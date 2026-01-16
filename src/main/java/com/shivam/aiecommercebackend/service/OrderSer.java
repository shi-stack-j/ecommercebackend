package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.order.CreateOrderDto;
import com.shivam.aiecommercebackend.dto.order.OrderResponseDto;
import com.shivam.aiecommercebackend.entity.*;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import com.shivam.aiecommercebackend.exception.InSufficientStockException;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.OrderMap;
import com.shivam.aiecommercebackend.repository.*;
import com.shivam.aiecommercebackend.utility.OrderSpecifications;
import com.shivam.aiecommercebackend.utility.PriceCalculator;
import com.shivam.aiecommercebackend.utility.TrackingNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderSer {
    @Autowired
    private OrderEnRepo orderRepo;
    @Autowired
    private UserEnRepo userRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductEnRepo productEnRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    //    Place Order (MOST IMPORTANT)
    @PreAuthorize("#userId==principal.id")
    @Transactional
    public OrderResponseDto placeOrder(Long userId, CreateOrderDto orderDto){
        if(userId==null || userId<=0)throw new InvalidInputException("User Id cannot be NULL or Zero . Provide valid userId");
        if(orderDto==null)throw new InvalidInputException("Order Dto cannot be null");
        if(orderDto.getAddress()==null || orderDto.getAddress().isBlank())throw new InvalidInputException("Address cannot be Null or Empty");
        if(orderDto.getPaymentMode()==null)throw new InvalidInputException("Payment Method cannot be null");
        UserEn userEn=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Not found for the given id :- "+userId));
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseThrow(()->new InvalidInputException("Cart is empty"));
        if(cartEn.getItems().isEmpty()) throw new InvalidInputException("Cart is Empty");
        OrderEn orderEn=new OrderEn();
        orderEn.setAddress(orderDto.getAddress().trim());
        orderEn.setPaymentMode(orderDto.getPaymentMode());
        orderEn.setUserEn(userEn);
        List<OrderItem> items=new ArrayList<>();
        Integer totalItems=0;
        BigDecimal totalPrice=BigDecimal.ZERO;
        for(CartItem cartItem:cartEn.getItems().values()){
            ProductEn productEn=cartItem.getProductEn();
            if(!productEn.getAvailability())throw new InvalidInputException("This product is not available ");
            if(productEn.getQuantity()<cartItem.getQuantity())throw new InSufficientStockException("Stock is not sufficient for product :- "+productEn.getName());
            productEn.setQuantity(productEn.getQuantity()-cartItem.getQuantity());
            BigDecimal latestPrice= PriceCalculator.calculateDiscountedPrice(
                    productEn.getPrice(),
                    productEn.getDiscount()
            );
            BigDecimal latestTotalPrice=latestPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            OrderItem orderItem=new OrderItem();
            orderItem.setOrderEn(orderEn);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(latestPrice);
            orderItem.setProductEn(productEn);
            orderItem.setReturnCapabilities(productEn.getCategoryEn().getPolicyEn().getCapabilities());
            orderItem.setWindowSize(productEn.getCategoryEn().getPolicyEn().getWindowSize());
            items.add(orderItem);

            totalItems+=cartItem.getQuantity();
            totalPrice=totalPrice.add(latestTotalPrice);
        }
        orderEn.setTotalAmount(totalPrice);
        orderEn.setTotalItems(totalItems);
        orderEn.setOrderStatus(OrderStatus.PENDING);
        orderEn.setItems(items);
        orderRepo.save(orderEn);

        cartEn.getItems().clear();
        cartEn.calculateTotal();
        return OrderMap.toResponseDto(orderEn);
    }
//    Get Order By ID
    @PreAuthorize("@auth.canAccessOrder(#orderID)")
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderByID(Long orderID){
        if(orderID==null || orderID<=0)throw new InvalidInputException("Enter valid userId it cannot be null or empty....");
        OrderEn orderEn=orderRepo.findById(orderID)
                .orElseThrow(()->new ResourceNotFoundException("Order Not found with the given id :- "+orderID));
        return OrderMap.toResponseDto(orderEn);
    }


//    Get Orders of User
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or #userId==principal.id")
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrderOfUser(Long userId,Integer page,Integer size){
        if(userId==null || userId<=0)
            throw new InvalidInputException("UserId cannot be null or less then equals to zero");
        if(page == null || page < 0)
            throw new InvalidInputException("Page cannot be null or negative");

        if(size == null || size <= 0)
            throw new InvalidInputException("Size must be greater than zero");

        if(size > 100)
            throw new InvalidInputException("Size cannot exceed 100");

        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<OrderEn> userOrders=orderRepo.findByUserEn_Id(userId,pageable);
        Page<OrderResponseDto> orderResponse=userOrders.map(OrderMap::toResponseDto);
        return orderResponse;
    }

//    Get All Orders (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(
            PaymentMode mode,
            LocalDate fromDate,
            LocalDate toDate,
            OrderStatus orderStatus,
            BigDecimal minimumAmount,
            BigDecimal maxAmount,
            Integer page,
            Integer size
    ){
        if(page==null || page<0)throw new InvalidInputException("Page cannot be null or less then");
        if(size==null || size<=0 || size>100) throw new InvalidInputException("Page cannot be null or zero of greater then 100");
        if (minimumAmount != null && minimumAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Minimum amount cannot be negative");
        }

        if (maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Maximum amount cannot be negative");
        }

        if (minimumAmount != null && maxAmount != null &&
                minimumAmount.compareTo(maxAmount) > 0) {
            throw new InvalidInputException("Minimum amount cannot be greater than maximum amount");
        }

        if (fromDate != null && toDate != null &&
                fromDate.isAfter(toDate)) {
            throw new InvalidInputException("From date cannot be after To date");
        }
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdAt").descending());
        Specification<OrderEn> specification= OrderSpecifications.buildFilter(
                mode,
                fromDate,
                toDate,
                orderStatus,
                minimumAmount,
                maxAmount
        );
        Page<OrderEn> orderEns=orderRepo.findAll(specification,pageable);
        return orderEns.map(OrderMap::toResponseDto);
    }

//    Update Order Status (Admin/Manager)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId,OrderStatus newStatus){
        if(orderId==null || orderId<=0)throw new InvalidInputException("Provide valid OrderId it cannot be null or greater then or equals to zero");
        if(newStatus==null )throw new InvalidInputException("Provide valid new Status");
        OrderEn orderEn=orderRepo.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with the given id :- "+orderId));
        validateNewStatus(orderEn.getOrderStatus(),newStatus);
        if(newStatus.equals(orderEn.getOrderStatus()))throw new InvalidInputException("Status is same as current status");
        if(newStatus.equals(OrderStatus.SHIPPED) && orderEn.getTrackingNumber()==null){
            orderEn.setTrackingNumber(TrackingNumberGenerator.generate());
        }
        if(newStatus.equals(OrderStatus.DELIVERED) && orderEn.getDeliveredAt()==null){
            orderEn.setDeliveredAt(LocalDateTime.now());
        }
        orderEn.setOrderStatus(newStatus);
        return OrderMap.toResponseDto(orderEn);
    }

//    This is the method that is used to validateNewStatus
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")

    public void validateNewStatus(OrderStatus current,OrderStatus newStatus){
        if (current == OrderStatus.CANCELLED || current == OrderStatus.DELIVERED) {
            throw new InvalidInputException("Order cannot be updated after final state");
        }

        if (current == OrderStatus.PENDING && newStatus == OrderStatus.DELIVERED) {
            throw new InvalidInputException("Order must be shipped before delivery");
        }
    }
//    Cancel Order
//    Checked
    @PreAuthorize("@auth.canAccessOrder(#orderId)")
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId){
        if(orderId==null || orderId<=0)throw new InvalidInputException("Order Id cannot be null Or less then equals to zero");
        OrderEn orderEn=orderRepo.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with the given id :- "+orderId));
        if(orderEn.getOrderStatus().equals(OrderStatus.CANCELLED) || orderEn.getOrderStatus().equals(OrderStatus.DELIVERED)){
            throw new InvalidInputException("Order cannot be updated after final state");
        }
        orderEn.setOrderStatus(OrderStatus.CANCELLED);
        orderRepo.save(orderEn);
        return OrderMap.toResponseDto(orderEn);
    }
}

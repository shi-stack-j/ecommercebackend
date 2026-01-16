package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.order.OrderItemResponseDto;
import com.shivam.aiecommercebackend.dto.order.OrderResponseDto;
import com.shivam.aiecommercebackend.entity.OrderEn;
import com.shivam.aiecommercebackend.entity.OrderItem;
import jakarta.persistence.criteria.Order;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.util.List;

public final class  OrderMap {
    public static OrderResponseDto toResponseDto(OrderEn orderEn){
        OrderResponseDto responseDto=new OrderResponseDto();
        responseDto.setOrderId(orderEn.getId());
        responseDto.setAddress(orderEn.getAddress());
        responseDto.setTotalAmount(orderEn.getTotalAmount());
        responseDto.setOrderStatus(orderEn.getOrderStatus());
        responseDto.setPaymentMode(orderEn.getPaymentMode());
        responseDto.setCreatedAt(orderEn.getCreatedAt());
        responseDto.setUpdatedAt(orderEn.getUpdatedAt());
        responseDto.setDeliveredAt(orderEn.getDeliveredAt()==null ? null:orderEn.getDeliveredAt());
        responseDto.setTrackingNumber(orderEn.getTrackingNumber()==null || orderEn.getTrackingNumber().isBlank() ?"Tracking id will we generated Once Order Shipped":orderEn.getTrackingNumber());
        List<OrderItemResponseDto> orderItemResponseDto=orderEn
                .getItems()
                .stream()
                .map(OrderMap::orderItemResponseDto)
                .toList();
        responseDto.setResponseDtoList(orderItemResponseDto);
        responseDto.setTotalItems(orderEn.getTotalItems());
        return responseDto;
    }
    public static OrderItemResponseDto orderItemResponseDto(OrderItem orderItem){
        return OrderItemResponseDto.builder()
                .productId(orderItem.getProductEn().getId())
                .productName(orderItem.getProductEn().getName())
                .priceAtPurchase(orderItem.getPrice())
                .subTotal(orderItem
                        .getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .quantity(orderItem.getQuantity())
                .windowSize(orderItem.getWindowSize())
                .returnCapabilities(orderItem.getReturnCapabilities().toString())
                .itemId(orderItem.getId())
                .build();
    }

}

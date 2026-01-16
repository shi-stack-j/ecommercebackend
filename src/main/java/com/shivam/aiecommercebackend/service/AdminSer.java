package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.dto.returnDtos.ReturnResponseDto;
import com.shivam.aiecommercebackend.entity.*;
import com.shivam.aiecommercebackend.enums.*;
import com.shivam.aiecommercebackend.exception.*;
import com.shivam.aiecommercebackend.mapper.ReturnMap;
import com.shivam.aiecommercebackend.mapper.UserMap;
import com.shivam.aiecommercebackend.repository.OrderItemRepo;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import com.shivam.aiecommercebackend.repository.ReturnRepo;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class AdminSer {
    private static final Logger log = LoggerFactory.getLogger(AdminSer.class);
    @Autowired
    private UserEnRepo userEnRepo;
    @Autowired
    private OrderItemRepo orderItem;
    @Autowired
    private ReturnRepo returnRepo;
    @Autowired
    private ProductEnRepo productRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    //    Update User Role
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ApiResponseDto updateUserRole(String username, RoleEnum roleEnum){
        if(username==null || username.isBlank())throw new InvalidInputException("Username is not valid");
        UserEn userEn=userEnRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given username :- "+username));
        if(userEn.getRole().equals(roleEnum)){
            return ApiResponseDto.builder()
                    .message("Role is a;ready same")
                    .time(LocalDateTime.now())
                    .status(ApiResponseStatusEnum.SUCCESS)
                    .build();
        }
        userEn.setRole(roleEnum);
        return ApiResponseDto.builder()
                .message("Role updated successfully")
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }
    //    Get All Users
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> getAllUsers(Integer page,Integer size){
        if(page==null || page<0)throw new InvalidInputException("Page number is not valid");
        if(size==null || size<=0|| size>100)throw new InvalidInputException("Size is not valid ");
        Pageable pageable=PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<UserEn> users=userEnRepo.findAll(pageable);
        return users.map(UserMap::toUserDto);
    }
    //    Get User By id
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getUser(String value,String type){
        if (value == null || value.isBlank() || type == null || type.isBlank()) throw new InvalidInputException("Value or Type is not value");
        UserEn userEn;
        type = type.trim().toLowerCase();
        String finalValue=value.trim();
        switch (type) {
            case "id":
                if (!value.matches("\\d+")) {
                    throw new InvalidInputException("Id must be numeric");
                }
                userEn=userEnRepo.findById(Long.valueOf(finalValue))
                        .orElseThrow(()->new ResourceNotFoundException("User not found with the given id :- "+value));
                break;
            case "username":
                userEn = userEnRepo.findByUsernameIgnoreCase(finalValue)
                        .orElseThrow(()->new ResourceNotFoundException("User not found with the given username :- "+value.trim()));
                break;
            case "email":
                if (!finalValue.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new InvalidInputException("Enter valid email");
                }
                userEn = userEnRepo.findByEmailIgnoreCase(finalValue)
                        .orElseThrow(()->new ResourceNotFoundException("User not found with the given email :- "+value.trim()));
                break;
            default:
                throw new InvalidInputException("Type is not valid :- "+type);
        }
        return UserMap.toUserDto(userEn);
    }
//    Return Services

    //       ├── approveReturn()
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ReturnResponseDto approveReturn(Long returnId){
//        To check return id is valid or not
        if(returnId==null || returnId<=0)throw new InvalidInputException("Return id is not valid");
//        Checking return id found or not
        ReturnEn returnEn=returnRepo.findById(returnId)
                .orElseThrow(()->new ResourceNotFoundException("Return Record not found with the given id "+returnId));

//        Checking that Status can be changed to approved or not
        ReturnStatusEnum currentStatus = returnEn.getReturnStatusEnum();
        if(currentStatus == ReturnStatusEnum.COMPLETED || currentStatus == ReturnStatusEnum.REJECTED) {
            throw new InvalidInputException("Cannot change the status of a completed or rejected request");
        }

//        Checking that request for return is valid or not
        ReturnPolicyCapabilities policy = returnEn.getOrderItem().getReturnCapabilities();
        ReturnTypeEnum requestedType = returnEn.getReturnTypeEnm();

        if ((policy == ReturnPolicyCapabilities.REFUND_ONLY && requestedType == ReturnTypeEnum.REPLACEMENT) ||
                (policy == ReturnPolicyCapabilities.REPLACEMENT_ONLY && requestedType == ReturnTypeEnum.REFUND)) {
            throw new InvalidInputException("This return type is not allowed for this item as per policy");
        }

//        This to check weather the item is delivered or not
        LocalDateTime deliveredAt = returnEn.getOrderItem().getOrderEn().getDeliveredAt();
        if(deliveredAt == null) throw new InvalidInputException("Order is not delivered yet");
//        Checking that request raised in valid window
        long daysPassed = ChronoUnit.DAYS.between(deliveredAt, LocalDateTime.now());
        if(daysPassed > returnEn.getOrderItem().getWindowSize()) {
            throw new ReturnWindowExpirationException("Return window has expired for this item");
        }
//        Checking the another request exists by this or not
        boolean exists = returnRepo.existsByOrderItem_IdAndReturnStatusEnumIn(
                returnEn.getOrderItem().getId(),
                List.of(ReturnStatusEnum.REQUESTED, ReturnStatusEnum.APPROVED)
        );
        if(exists && currentStatus != ReturnStatusEnum.REQUESTED) {
            throw new DuplicationEntryException("Another active return request exists for this item");
        }


        returnEn.setReturnStatusEnum(ReturnStatusEnum.APPROVED);
        returnEn.setProcessedAt(LocalDateTime.now());
        returnRepo.save(returnEn);

        return ReturnMap.toResponseDto(returnEn);

    }
//       ├── rejectReturn()
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ReturnResponseDto rejectReturn(Long returnId) {
        if(returnId == null || returnId <= 0)
            throw new InvalidInputException("Return id is not valid");

        // Fetch the return request
        ReturnEn returnEn = returnRepo.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException("Return record not found with id: " + returnId));

        // Cannot reject if already completed or rejected
        if(returnEn.getReturnStatusEnum() == ReturnStatusEnum.COMPLETED ||
                returnEn.getReturnStatusEnum() == ReturnStatusEnum.REJECTED) {
            throw new InvalidInputException("Cannot reject a completed or already rejected request");
        }

        // Update status to REJECTED
        returnEn.setReturnStatusEnum(ReturnStatusEnum.REJECTED);
        returnEn.setProcessedAt(LocalDateTime.now());
        returnRepo.save(returnEn);

        return ReturnMap.toResponseDto(returnEn);
    }
//       ├── completeReturn()
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ReturnResponseDto completeReturn(Long returnId) {
    if(returnId == null || returnId <= 0)
        throw new InvalidInputException("Return id is not valid");

    // Fetch the return request
    ReturnEn returnEn = returnRepo.findById(returnId)
            .orElseThrow(() -> new ResourceNotFoundException("Return record not found with id: " + returnId));

    // Check current status
    if(returnEn.getReturnStatusEnum() == ReturnStatusEnum.REJECTED ||
            returnEn.getReturnStatusEnum() == ReturnStatusEnum.COMPLETED) {
        throw new InvalidInputException("Cannot complete a rejected or already completed request");
    }

    if(returnEn.getReturnStatusEnum() != ReturnStatusEnum.APPROVED) {
        throw new InvalidInputException("Only approved requests can be completed");
    }

    OrderItem orderItem = returnEn.getOrderItem();

    // Handle action based on return type
    if(returnEn.getReturnTypeEnm() == ReturnTypeEnum.REFUND) {
        executeRefund(returnId);
        // Process refund logic (e.g., call payment gateway)
        // Here you can call your PaymentService.refund(orderItem)
    } else if(returnEn.getReturnTypeEnm() == ReturnTypeEnum.REPLACEMENT) {
        // Process replacement logic
        executeReplacement(returnId);
        // Usually: update stock, maybe create a new OrderItem for replacement
        ProductEn product = orderItem.getProductEn();
        product.setQuantity(product.getQuantity() + orderItem.getQuantity()); // Adding back the stock for replacement
        productRepo.save(product);
    }

    // Mark return as completed
    returnEn.setReturnStatusEnum(ReturnStatusEnum.COMPLETED);
    returnEn.setProcessedAt(LocalDateTime.now());
    returnRepo.save(returnEn);

    return ReturnMap.toResponseDto(returnEn);
}
//       └── getPendingReturns()
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ReturnResponseDto> getPendingReturns(Integer page,Integer size){
        if(page==null || page<0)throw new InvalidInputException("Page is number valid");
        if(size==null || size>100 || size<0)throw new InvalidInputException("Size value is not valid");
        Pageable pageable=PageRequest.of(page,size,Sort.by("requestedAt").descending());
        Page<ReturnEn> returnEns=returnRepo.findByReturnStatusEnum(ReturnStatusEnum.REQUESTED,pageable);
        return returnEns.map(ReturnMap::toResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    private void executeRefund(Long returnId){
        if(returnId==null || returnId<=0)throw new InvalidInputException("Return id is not valid");
        ReturnEn returnEn=returnRepo.findById(returnId)
                .orElseThrow(()->new ResourceNotFoundException("Return Request not found with the given entity"));

        ReturnPolicyCapabilities policy=returnEn.getOrderItem().getReturnCapabilities();
        ReturnTypeEnum requestedType=returnEn.getReturnTypeEnm();

        if ((policy == ReturnPolicyCapabilities.REFUND_ONLY && requestedType == ReturnTypeEnum.REPLACEMENT) ||
                (policy == ReturnPolicyCapabilities.REPLACEMENT_ONLY && requestedType == ReturnTypeEnum.REFUND)) {
            throw new InvalidInputException("This return type is not allowed for this item as per policy");
        }
        if(requestedType!=ReturnTypeEnum.REFUND)throw new InvalidInputException("This is replacement request cannot process it inside the Refund executor");
        RefundPaymentEn refundPaymentEn = new RefundPaymentEn();
        refundPaymentEn.setRefundAmount(returnEn.getRefundableAmount());
        refundPaymentEn.setRefundMode(RefundModeEnum.MANUAL); // Or detect payment mode dynamically
        refundPaymentEn.setRefundStatus(RefundStatusEnum.SUCCESS); // Assume success
        refundPaymentEn.setReturnEn(returnEn);
        refundPaymentEn.setCompletedAt(LocalDateTime.now());

        returnEn.setRefundPayment(refundPaymentEn);
        // 5️⃣ Mark return request as completed
        returnEn.setReturnStatusEnum(ReturnStatusEnum.COMPLETED);
        returnEn.setProcessedAt(LocalDateTime.now());

        // 6️⃣ Save everything
        returnRepo.save(returnEn);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    private void executeReplacement(Long returnId) {
        if(returnId == null || returnId <= 0)
            throw new InvalidInputException("Return id is not valid");

        ReturnEn returnEn = returnRepo.findById(returnId)
                .orElseThrow(() -> new ResourceNotFoundException("Return Request not found with id: " + returnId));

        ReturnPolicyCapabilities policy = returnEn.getOrderItem().getReturnCapabilities();
        ReturnTypeEnum requestedType = returnEn.getReturnTypeEnm();

        // Validate that replacement is allowed
        if ((policy == ReturnPolicyCapabilities.REFUND_ONLY && requestedType == ReturnTypeEnum.REPLACEMENT) ||
                (policy == ReturnPolicyCapabilities.REPLACEMENT_ONLY && requestedType == ReturnTypeEnum.REFUND)) {
            throw new InvalidInputException("This return type is not allowed for this item as per policy");
        }

        if(requestedType != ReturnTypeEnum.REPLACEMENT)
            throw new InvalidInputException("This is a refund request. Cannot process inside replacement executor");

        OrderItem oldItem = returnEn.getOrderItem();

        // 1️⃣ Create a new replacement OrderItem
        OrderItem replacementItem = new OrderItem();
        replacementItem.setProductEn(oldItem.getProductEn());
        replacementItem.setOrderEn(oldItem.getOrderEn()); // Same order for simplicity; or create separate order if needed
        replacementItem.setPrice(oldItem.getPrice());
        replacementItem.setQuantity(oldItem.getQuantity());
        replacementItem.setReturnCapabilities(oldItem.getReturnCapabilities());
        replacementItem.setWindowSize(oldItem.getWindowSize());
        replacementItem.setReturnRequestedAt(LocalDateTime.now());

        // 2️⃣ Save the new OrderItem
        orderItemRepo.save(replacementItem);

        // 3️⃣ Link replacement OrderItem to ReturnEn
        returnEn.setReplacementOrderItem(replacementItem);

        // 4️⃣ Mark return request as completed
        returnEn.setReturnStatusEnum(ReturnStatusEnum.COMPLETED);
        returnEn.setProcessedAt(LocalDateTime.now());

        returnRepo.save(returnEn);

        // Optional: notify user about replacement shipment
        // notificationService.sendReplacementProcessedNotification(returnEn.getUserEn(), replacementItem);
    }

}

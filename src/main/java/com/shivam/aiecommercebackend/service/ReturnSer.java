package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.returnDtos.ReturnRequestDto;
import com.shivam.aiecommercebackend.dto.returnDtos.ReturnResponseDto;
import com.shivam.aiecommercebackend.entity.OrderItem;
import com.shivam.aiecommercebackend.entity.ReturnEn;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import com.shivam.aiecommercebackend.enums.ReturnStatusEnum;
import com.shivam.aiecommercebackend.enums.ReturnTypeEnum;
import com.shivam.aiecommercebackend.exception.*;
import com.shivam.aiecommercebackend.mapper.ReturnMap;
import com.shivam.aiecommercebackend.repository.OrderItemRepo;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import com.shivam.aiecommercebackend.repository.ReturnRepo;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import com.shivam.aiecommercebackend.utility.RefundCalculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReturnSer {
    @Autowired
    private ReturnRepo returnRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private ProductEnRepo productRepo;
    @Autowired
    private UserEnRepo userRepo;

    @PreAuthorize("@auth.canSeeReturnRequest(#returnId)")
    @Transactional
    public ReturnResponseDto getReturnRequest(Long returnId,Long userId){
        if(returnId==null || returnId<=0)throw new InvalidInputException("Return id is not valid");
        if(userId==null || userId<=0)throw new InvalidInputException("User id is not valid");
        ReturnEn returnEn=returnRepo.findById(returnId)
                .orElseThrow(()->new ResourceNotFoundException("Return not found with the given id :- "+returnId));
        if(!returnEn.getUserEn().getId().equals(userId))throw new AccessDeniedException("Cannot access this return ");
        return ReturnMap.toResponseDto(returnEn);
    }

    @PreAuthorize("@auth.canRaiseRequest(#requestDto)")
    @Transactional
    public ReturnResponseDto raiseReturnRequest(Long userId, ReturnRequestDto requestDto){
        if(requestDto==null ) throw new InvalidInputException("Request Dto cannot be null");
        if(userId==null || userId<=0)throw new InvalidInputException("UserID is not valid");
        OrderItem orderItem=orderItemRepo.findById(requestDto.getOrderItemId())
                .orElseThrow(()->new ResourceNotFoundException("OrderItem not found with the given id :- "+requestDto.getOrderItemId()));
        UserEn userEn=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given id :- "+userId));
        if(!orderItem.getOrderEn().getUserEn().getId().equals(userId)) throw new AccessDeniedException("Order item does not belong to user");
        if(orderItem.getReturnCapabilities().equals(ReturnPolicyCapabilities.NONE))throw new InvalidInputException("This item is not returnable");
//        To check for the valid request or not
        validateReturnType(orderItem.getReturnCapabilities(),requestDto.getReturnType());
//        To check for the window size
        LocalDateTime deliveredAt=orderItem.getOrderEn().getDeliveredAt();
        if(deliveredAt==null)throw new InvalidInputException("Order is not delivered yet");
        Long daysPassed= ChronoUnit.DAYS.between(deliveredAt,LocalDateTime.now());
        if(daysPassed>orderItem.getWindowSize()) throw new ReturnWindowExpirationException("Return window closed");
//        To check for the duplicate entry
        boolean isAlreadyExists=returnRepo.existsByOrderItem_IdAndReturnStatusEnumIn(
                requestDto.getOrderItemId(),
                List.of(ReturnStatusEnum.REQUESTED,
                ReturnStatusEnum.APPROVED,
                ReturnStatusEnum.COMPLETED,
                ReturnStatusEnum.INITIATED
                )
        );
        if(isAlreadyExists)throw new DuplicationEntryException("A return request is already active for this item");
        BigDecimal refundableAmount= RefundCalculator.calculateRefundableAmount(orderItem,orderItem.getQuantity(),BigDecimal.valueOf(5));
        ReturnEn returnEn=new ReturnEn();
        returnEn.setReason(requestDto.getReason());
        returnEn.setUserEn(userEn);
        returnEn.setOrderItem(orderItem);
        returnEn.setReturnTypeEnm(requestDto.getReturnType());
        returnEn.setReturnStatusEnum(ReturnStatusEnum.REQUESTED);
        returnEn.setRequestedAt(LocalDateTime.now());
        returnEn.setRefundableAmount(refundableAmount);
        returnRepo.save(returnEn);
//        This is to set the return request raised time for the orderItem
        orderItem.setReturnRequestedAt(LocalDateTime.now());
        return ReturnMap.toResponseDto(returnEn);
    }
//    This method is only to help to check weather the request is valid or not
    private void validateReturnType(ReturnPolicyCapabilities policy, ReturnTypeEnum requested) {
        if (policy == ReturnPolicyCapabilities.NONE) {
            throw new InvalidInputException("This item is not returnable");
        }

        if (policy == ReturnPolicyCapabilities.REFUND_ONLY
                && requested != ReturnTypeEnum.REFUND) {
            throw new InvalidInputException("Only refund is allowed for this item");
        }

        if (policy == ReturnPolicyCapabilities.REPLACEMENT_ONLY
                && requested != ReturnTypeEnum.REPLACEMENT) {
            throw new InvalidInputException("Only replacement is allowed for this item");
        }
    }
}

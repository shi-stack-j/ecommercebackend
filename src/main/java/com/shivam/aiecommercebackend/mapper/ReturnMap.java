package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.returnDtos.ReturnResponseDto;
import com.shivam.aiecommercebackend.entity.ReturnEn;

public final class ReturnMap {
    public static ReturnResponseDto toResponseDto(ReturnEn returnEn){
        String msg;
        switch (returnEn.getReturnStatusEnum()){
            case REJECTED :
                    msg="Return request is rejected";
                    break;
            case APPROVED:
                    msg="Return request is approved";
                    break;
            case COMPLETED:
                    msg="Return request completed";
                    break;
            case INITIATED:
                    msg="Return Request is initiated";
                    break;
            case REQUESTED:
                    msg="Return Request is requested";
                    break;
            default:
                    msg="Cannot fetch the exact status";
        }
        return ReturnResponseDto.builder()
                .returnId(returnEn.getId())
                .requestedAt(returnEn.getRequestedAt())
                .message(msg)
                .orderItemId(returnEn.getOrderItem().getId())
                .returnStatusEnum(returnEn.getReturnStatusEnum())
                .returnTypeEnum(returnEn.getReturnTypeEnm())
                .refundableAmount(returnEn.getRefundableAmount())
                .reason(returnEn.getReason())
                .build();
    }
}

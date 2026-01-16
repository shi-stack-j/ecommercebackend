package com.shivam.aiecommercebackend.dto.order;

import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
//    orderId
    private Long orderId;
//            totalAmount
    private BigDecimal totalAmount;
//    totalItems
    private Integer totalItems;
//            orderStatus
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
//    paymentMode
    private PaymentMode paymentMode;
//            createdAt
    private LocalDateTime createdAt;
//    Updated At
    private LocalDateTime updatedAt;
//    trackingNumber
    private String trackingNumber;
//              address
    private String address;

    private LocalDateTime deliveredAt;

    private List<OrderItemResponseDto> responseDtoList=new ArrayList<>();


}

package com.shivam.aiecommercebackend.dto.order;

import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponseDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subTotal;
    private String returnCapabilities;
    private Integer windowSize;
    private Long itemId;
//    productId
//            productName
//    priceAtPurchase
//            quantity
//    subtotal

}

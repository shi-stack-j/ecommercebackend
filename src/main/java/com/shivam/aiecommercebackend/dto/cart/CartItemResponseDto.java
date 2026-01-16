package com.shivam.aiecommercebackend.dto.cart;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponseDto {
    //    productId
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Boolean availability;
    private BigDecimal subTotal;
//
//productName
//
//price (current price)
//
//quantity
//
//subtotal (price × quantity)
//
//availability (optional but good UX)
}

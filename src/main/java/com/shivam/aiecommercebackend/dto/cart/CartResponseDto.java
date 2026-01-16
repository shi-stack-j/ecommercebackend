package com.shivam.aiecommercebackend.dto.cart;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponseDto> products=new ArrayList<>();
    private Integer totalItems;
    private Boolean isEmpty;
    private BigDecimal totalAmount;
//    totalItems (sum of quantities)
//
//    isEmpty
}

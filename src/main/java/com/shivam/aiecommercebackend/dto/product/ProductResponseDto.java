package com.shivam.aiecommercebackend.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.BiPredicate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponseDto {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal discount;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private String imageUrl;
    private Boolean availability;
    private Integer quantity;
    private String returnCapabilities;
    private Integer windowSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

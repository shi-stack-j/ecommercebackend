package com.shivam.aiecommercebackend.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDto {
    @NotBlank
    @Size(max=30 ,message = "Size should not be greater then 30")
    private String name;
    @NotBlank
    @Size(min=5, max = 200 )
    private String description;
    @PositiveOrZero
    private Integer quantity;
    @PositiveOrZero
    @Max(100)
    private BigDecimal discount;
    @PositiveOrZero
    private BigDecimal price;

    private Boolean availability;
}

package com.shivam.aiecommercebackend.dto.review;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewRequestDto {
    @NotNull(message = "Rating required")
    @Min(1)
    @Max(5)
    @Positive
    private int rating;
    @Size(min = 5, max = 50,message = "Description is limited to 5-50")
    private String description;
}

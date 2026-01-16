package com.shivam.aiecommercebackend.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequestDto {

    @Size(max=100,message = "Description cannot be greater then 100")
    private String description;

    @Min(value = 1,message = "Rating cannot be less then 1")
    @Max(value = 5,message = "Rating cannot be greater then 5")
    private Integer rating;
}

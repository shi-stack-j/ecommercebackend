package com.shivam.aiecommercebackend.dto.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "description is required")
    @Size(min = 10, max = 100)
    private String description;
    @Positive
    @NotNull(message = "Policy id is must ")
    private Long policyId;
}

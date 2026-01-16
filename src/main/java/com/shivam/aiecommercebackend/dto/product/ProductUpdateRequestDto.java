package com.shivam.aiecommercebackend.dto.product;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDto {
    @Size(max = 100, message = "Name could not exceeds 100")
    private String name;
    @Size(max = 500,message = "Description could not exceeds 500")
    private String description;
}

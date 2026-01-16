package com.shivam.aiecommercebackend.dto.category;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequestDto {
    @Size(max=50,message = "Name could be only of length 50")
    private String name;
    @Size(max=100,message = "Description could only be of length 100")
    private String description;
}

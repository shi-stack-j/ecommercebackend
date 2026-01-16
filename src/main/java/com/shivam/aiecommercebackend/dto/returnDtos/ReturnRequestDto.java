package com.shivam.aiecommercebackend.dto.returnDtos;


import com.shivam.aiecommercebackend.enums.ReturnTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReturnRequestDto {
    @NotBlank(message = "Reason cannot be null")
    @Size(min = 20,max = 100,message = "Message size must be b/w 20 - 100")
    private String reason;
    @NotNull(message = "Order id cannot be null")
    @Positive(message = "Order id must be positive ")
    private Long orderItemId;
    @NotNull(message = "Return type cannot be null")
    private ReturnTypeEnum returnType;
}

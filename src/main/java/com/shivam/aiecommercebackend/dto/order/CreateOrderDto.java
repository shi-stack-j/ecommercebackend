package com.shivam.aiecommercebackend.dto.order;

import com.shivam.aiecommercebackend.enums.PaymentMode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotBlank(message = "Address cannot be Empty")
    @Size(min = 10, max = 255, message = "Address must be between 10 and 255 characters")
    private String address;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment mode required")
    private PaymentMode paymentMode;
}

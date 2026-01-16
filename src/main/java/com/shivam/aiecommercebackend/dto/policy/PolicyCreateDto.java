package com.shivam.aiecommercebackend.dto.policy;

import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyCreateDto {
    @PositiveOrZero
    @Min(value = 0, message = "Window size cannot be less than zero")
    @Max(value = 16, message = "Window size cannot be greater than 16")
    @NotNull
    private Integer windowSize;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ReturnPolicyCapabilities capabilities;
    @NotNull(message = "Policy name Must not be NULL")
    @NotBlank(message = "Policy Name must not be BLANK")
    @Size(min = 2,message = "Size must be greater then 2")
    private String policyName;

}

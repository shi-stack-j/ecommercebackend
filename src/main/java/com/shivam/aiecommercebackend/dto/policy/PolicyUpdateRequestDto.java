package com.shivam.aiecommercebackend.dto.policy;


import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyUpdateRequestDto {

    @Min(value = 0, message = "Window size cannot be less than zero")
    @Max(value = 16, message = "Window size cannot be greater than 16")
    private Integer newWindowSize;

    @Enumerated(EnumType.STRING)
    private ReturnPolicyCapabilities newPolicyCapabilities;

    private Boolean active;

}

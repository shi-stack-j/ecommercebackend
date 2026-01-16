package com.shivam.aiecommercebackend.dto.policy;


import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnPolicy {
    private ReturnPolicyCapabilities capabilities;
    private int windowSize;
}

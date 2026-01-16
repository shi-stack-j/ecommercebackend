package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.policy.PolicyDto;
import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;

public final class PolicyMap {
    public static PolicyDto toPolicyDto(ReturnPolicyEn policyEn){
        return PolicyDto.builder()
                .id(policyEn.getId())
                .active(policyEn.getActive())
                .capabilities(policyEn.getCapabilities().toString())
                .createdAt(policyEn.getCreatedAt())
                .updatedAt(policyEn.getUpdatedAt())
                .windowSize(policyEn.getWindowSize())
                .policyName(policyEn.getPolicyName())
                .build();
    }
}

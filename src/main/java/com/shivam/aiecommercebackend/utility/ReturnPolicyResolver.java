package com.shivam.aiecommercebackend.utility;

import com.shivam.aiecommercebackend.dto.policy.ReturnPolicy;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;
import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;

public final class ReturnPolicyResolver {
    public static ReturnPolicy policyResolver(ProductEn productEn){
        if(productEn.getCategoryEn()==null || productEn.getCategoryEn().getPolicyEn()==null ){
            return new ReturnPolicy(ReturnPolicyCapabilities.NONE,0);
        }
        ReturnPolicyEn returnPolicyEn=productEn.getCategoryEn().getPolicyEn();
        return new ReturnPolicy(returnPolicyEn.getCapabilities(),returnPolicyEn.getWindowSize());
    }
}

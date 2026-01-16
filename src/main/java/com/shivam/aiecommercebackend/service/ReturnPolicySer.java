package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.policy.PolicyCreateDto;
import com.shivam.aiecommercebackend.dto.policy.PolicyDto;
import com.shivam.aiecommercebackend.dto.policy.PolicyUpdateRequestDto;
import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;
import com.shivam.aiecommercebackend.exception.DuplicationEntryException;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.PolicyMap;
import com.shivam.aiecommercebackend.repository.ReturnPolicyRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnPolicySer {
    @Autowired
    private ReturnPolicyRepo policyRepo;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PolicyDto createPolicy(PolicyCreateDto createDto){
        if(createDto==null)throw new InvalidInputException("Policy Create Request dto cannot be null");
        boolean isExists=policyRepo.existsByWindowSizeAndCapabilities(createDto.getWindowSize(), createDto.getCapabilities());
        if(isExists) throw new DuplicationEntryException("Policy already exists for the given Capabilities and WindowSize");
        ReturnPolicyEn policyEn=new ReturnPolicyEn();
        policyEn.setCapabilities(createDto.getCapabilities());
        policyEn.setWindowSize(createDto.getWindowSize());
        policyEn.setPolicyName(createDto.getPolicyName());
        policyEn.setActive(true);
        ReturnPolicyEn savedPolicy=policyRepo.save(policyEn);
        return PolicyMap.toPolicyDto(savedPolicy);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional(readOnly = true)
    public PolicyDto getPolicy(Long policyId){
        if(policyId==null || policyId<=0)throw new InvalidInputException("Policy Id is not valid");
        ReturnPolicyEn policyEn=policyRepo.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy Not found with the given id :- "+policyId));
        return PolicyMap.toPolicyDto(policyEn);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PolicyDto deactivatePolicy(Long policyId){
        if(policyId==null || policyId<=0)throw new InvalidInputException("Policy Id is not valid");
        ReturnPolicyEn policyEn=policyRepo.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy Not found with the given id :- "+policyId));
        policyEn.setActive(false);
        return PolicyMap.toPolicyDto(policyEn);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PolicyDto activatePolicy(Long policyId){
        if(policyId==null || policyId<=0)throw new InvalidInputException("Policy Id is not valid");
        ReturnPolicyEn policyEn=policyRepo.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy Not found with the given id :- "+policyId));
        policyEn.setActive(true);
        return PolicyMap.toPolicyDto(policyEn);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public PolicyDto updatePolicy(Long policyId, PolicyUpdateRequestDto updateRequestDto){
        if(updateRequestDto==null)throw new InvalidInputException("Update Dto cannot be NULL");
        boolean isWindowSizeInValid= updateRequestDto.getNewWindowSize()==null ;
        boolean isActiveInValid = updateRequestDto.getActive()==null;
        boolean isCapabilitiesInValid = updateRequestDto.getNewPolicyCapabilities()==null;
        if(isCapabilitiesInValid && isActiveInValid && isWindowSizeInValid)throw new InvalidInputException("All fields cannot be null One is required");

        ReturnPolicyEn policyEn=policyRepo.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy not found for the given id :- "+policyId));
        if(!isCapabilitiesInValid && !policyEn.getCapabilities().equals(updateRequestDto.getNewPolicyCapabilities()))policyEn.setCapabilities(updateRequestDto.getNewPolicyCapabilities());
        if(!isActiveInValid && !(policyEn.getActive().equals(updateRequestDto.getActive())))policyEn.setActive(updateRequestDto.getActive());
        if(!isWindowSizeInValid && !(policyEn.getWindowSize().equals(updateRequestDto.getNewWindowSize())))policyEn.setWindowSize(updateRequestDto.getNewWindowSize());
        return PolicyMap.toPolicyDto(policyEn);
    }
}

package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;
import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnPolicyRepo extends JpaRepository<ReturnPolicyEn,Long> {
    boolean existsByWindowSizeAndCapabilities(Integer windowSize, ReturnPolicyCapabilities capabilities);
}

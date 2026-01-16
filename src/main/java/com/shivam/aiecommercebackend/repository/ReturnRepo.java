package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.ReturnEn;
import com.shivam.aiecommercebackend.enums.ReturnStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepo extends JpaRepository<ReturnEn,Long> {

    boolean existsByOrderItem_IdAndReturnStatusEnumIn(
            Long orderItemId,
            List<ReturnStatusEnum> statuses
    );
    Page<ReturnEn> findByReturnStatusEnum(ReturnStatusEnum returnStatusEnum, Pageable pageable);
    
}

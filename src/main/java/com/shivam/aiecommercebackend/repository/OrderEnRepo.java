package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.OrderEn;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEnRepo extends JpaRepository<OrderEn, Long> , JpaSpecificationExecutor<OrderEn> {
    Page<OrderEn> findByUserEn_Id(Long id, Pageable pageable);
    boolean existsByUserEn_IdAndOrderStatusAndItems_ProductEn_Id(
            Long userId,
            OrderStatus status,
            Long productId
    );

}

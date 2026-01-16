package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.OrderItem;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {


    boolean existsByOrderEn_UserEn_IdAndProductEn_IdAndOrderEn_OrderStatus(
            Long userId,
            Long productId,
            OrderStatus status
    );
}
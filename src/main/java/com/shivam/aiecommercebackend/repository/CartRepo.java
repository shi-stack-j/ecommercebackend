package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.CartEn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<CartEn,Long> {
    Optional<CartEn> findByUserEn_Id(Long id);
}

package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.ProductEn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEnRepo extends JpaRepository<ProductEn, Long> {
    Page<ProductEn> findAll(Pageable pageable);
    Page<ProductEn> findByCategoryEn_NameIgnoreCase(String name,Pageable pageable);
    Page<ProductEn> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name,String description,Pageable pageable);

    Page<ProductEn> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
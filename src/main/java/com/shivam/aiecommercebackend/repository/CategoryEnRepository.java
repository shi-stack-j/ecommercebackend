package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.CategoryEn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryEnRepository extends JpaRepository<CategoryEn, Long> {
    Optional<CategoryEn> findByNameIgnoreCase(String name);
}
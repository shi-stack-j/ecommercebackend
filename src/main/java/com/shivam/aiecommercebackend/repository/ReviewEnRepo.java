package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.ReviewEn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewEnRepo extends JpaRepository<ReviewEn, Long> {
    boolean existsByUserEn_IdAndProductEn_Id(Long userId, Long productId);
    Page<ReviewEn> findByProductEn_Id(Long id, Pageable pageable);
    Page<ReviewEn> findByUserEn_Id(Long id, Pageable pageable);
    @Query("SELECT AVG(r.rating) FROM ReviewEn r WHERE r.productEn.id = :id")
    Double findAverageByProductId(@Param("id") Long id);
}
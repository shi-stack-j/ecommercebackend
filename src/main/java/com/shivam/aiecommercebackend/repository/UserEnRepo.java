package com.shivam.aiecommercebackend.repository;

import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEnRepo extends JpaRepository<UserEn, Long> {
    Optional<UserEn> findByUsernameIgnoreCase(String username);
    Page<UserEn> findAll(Pageable pageable);
    Optional<UserEn> findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);
    boolean existsByRole(RoleEnum role);
}
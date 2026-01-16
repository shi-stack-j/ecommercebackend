package com.shivam.aiecommercebackend.utility;

import com.shivam.aiecommercebackend.dto.auth.UserRegisterDto;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.RoleEnum;
import com.shivam.aiecommercebackend.mapper.RegisterMap;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBootStrap implements CommandLineRunner {



    private final UserEnRepo userRepo;

    private final PasswordEncoder encoder;

    @Value("${default.admin.username}")
    private String adminName;
    @Value("${default.admin.password}")
    private String adminPassword;
    @Value("${default.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) throws Exception {
        boolean isAdminExists=userRepo.existsByRole(RoleEnum.ROLE_ADMIN);
        if(!isAdminExists){
            UserRegisterDto userRegisterDto=UserRegisterDto.builder()
                    .email(adminEmail)
                    .name("defaultAdmin")
                    .username(adminName)
                    .password(encoder.encode(adminPassword))
                    .build();

            UserEn userEn= RegisterMap.toUserEntity(userRegisterDto);
            userEn.setRole(RoleEnum.ROLE_ADMIN);
            userRepo.save(userEn);
            log.info("DefaultAdmin created Successfully....");
        }
    }
}

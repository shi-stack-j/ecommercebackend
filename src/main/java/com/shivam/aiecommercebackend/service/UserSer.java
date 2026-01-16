package com.shivam.aiecommercebackend.service;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.user.ProfileUpdateDto;
import com.shivam.aiecommercebackend.dto.user.UpdatePasswordRequestDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.UserMap;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSer {
    private static final Logger log = LoggerFactory.getLogger(UserSer.class);
    @Autowired
    private UserEnRepo userEnRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    //    Update User
    @PreAuthorize("hasRole('ADMIN') or #username==principal.username")
    @Transactional
    public UserDto updateUserProfile(ProfileUpdateDto dto, String username){
        if(username == null || username.isBlank())
            throw new InvalidInputException("Username is not valid");

        if(dto == null)
            throw new InvalidInputException("Profile dto is not valid");

        UserEn userEn = userEnRepo.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isNameInvalid = dto.getName() == null || dto.getName().isBlank();
        boolean isEmailInvalid = dto.getEmail() == null || dto.getEmail().isBlank();

        if(isEmailInvalid && isNameInvalid)
            throw new InvalidInputException("Both name and email cannot be empty");

        if(!isEmailInvalid){
            String email = dto.getEmail().trim();
            if(!userEn.getEmail().equalsIgnoreCase(email)
                    && userEnRepo.existsByEmailIgnoreCase(email)){
                throw new InvalidInputException("Email already in use");
            }
            userEn.setEmail(email);
        }

        if(!isNameInvalid){
            userEn.setName(dto.getName().trim());
        }

        return UserMap.toUserDto(userEn);
    }
    //    Disable User
    @PreAuthorize("hasRole('ADMIN') or #username==principal.username")
    @Transactional
    public ApiResponseDto changeUserAccountStatus(String username,Boolean status){
        if(username==null || username.isBlank())throw new InvalidInputException("Enter valid username");
        if(status==null )throw new InvalidInputException("New Status is not valid it cannot be null");
        UserEn userEn=userEnRepo.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given user name :- "+username));

//        This will check is CurrentStatus of User is equal to given Status then no need to update
            if(userEn.isEnabled()==status) {
                if(status)throw new InvalidInputException("Account Already Enabled");
                else throw new InvalidInputException("Account Already Disabled");
            }
//        If Given status is different then we will update it to new status
        userEn.setEnabled(status);
        userEnRepo.save(userEn);
        return ApiResponseDto.builder()
                .status(ApiResponseStatusEnum.SUCCESS)
                .time(LocalDateTime.now())
                .message("Status updated Successfully :-  "+(status?"Account Enabled ":"Account Disabled"))
                .build();
    }
//    Change password
    @PreAuthorize("hasRole('ADMIN') or #username==principal.username")
    @Transactional
    public ApiResponseDto changePassword(String username, UpdatePasswordRequestDto passwordRequestDto){
        if(username==null || username.isBlank())throw new InvalidInputException("Username is not valid");
        if(passwordRequestDto==null)throw new InvalidInputException("Update Request Dto cannot be null");
        UserEn userEn=userEnRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given username :- "+username));
        String originalPassword=userEn.getPassword();
        String userGivenPassword=passwordRequestDto.getCurrentPassword();
        log.info("Password checking result is :- "+passwordEncoder.matches(userGivenPassword,originalPassword));
        if(!passwordEncoder.matches(userGivenPassword,originalPassword))throw new InvalidInputException("Current password is not correct ");
        String newPassword=passwordEncoder.encode(passwordRequestDto.getNewPassword());
        userEn.setPassword(newPassword);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .message("Password updated Successfully")
                .build();
    }
}

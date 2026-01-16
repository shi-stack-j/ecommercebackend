package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.auth.UserRegisterDto;
import com.shivam.aiecommercebackend.entity.UserEn;

public class RegisterMap {
    public static UserEn toUserEntity(UserRegisterDto userRegisterDto){

        UserEn userEn=new UserEn();
        userEn.setEmail(userRegisterDto.getEmail());
        userEn.setName(userRegisterDto.getName());
        userEn.setUsername(userRegisterDto.getUsername());
        userEn.setPassword(userRegisterDto.getPassword());

        return userEn;
    }
}

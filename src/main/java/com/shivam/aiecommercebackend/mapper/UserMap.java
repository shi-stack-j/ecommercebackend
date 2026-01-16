package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.entity.UserEn;

public class UserMap {
    public static UserDto toUserDto(UserEn userEn){
//        UserDto userDto=new UserDto();
//        userDto.setEmail(userEn.getEmail());
//        userDto.setId(userEn.getId());
//        userDto.setName(userEn.getName());
//        userDto.setUsername(userEn.getUsername());
//        userDto.setEnabled(userEn.isEnabled());
//        userDto.setCreatedAt(userEn.getCreatedAt());
//        userDto.setUpdatedAt(userEn.getUpdatedAt());
//        userDto.setRole(userEn.getRole().name());
//        return userDto;
        return UserDto.builder()
                .email(userEn.getEmail())
                .id(userEn.getId())
                .name(userEn.getName())
                .username(userEn.getUsername())
                .createdAt(userEn.getCreatedAt())
                .isEnabled(userEn.isEnabled())
                .updatedAt(userEn.getUpdatedAt())
                .role(userEn.getRole().name())
                .build();
    }
}

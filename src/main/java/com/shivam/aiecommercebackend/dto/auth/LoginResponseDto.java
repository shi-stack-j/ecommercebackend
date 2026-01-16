package com.shivam.aiecommercebackend.dto.auth;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String  jwt;
    private String username;
}

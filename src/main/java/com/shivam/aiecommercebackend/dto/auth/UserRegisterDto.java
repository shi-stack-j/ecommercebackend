package com.shivam.aiecommercebackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email required ")
    @Email(message = "Enter valid email")
    private String email;
    @NotBlank(message = "Username is must")
    private String username;
    @NotBlank(message = "Password is must")
    @Size(min = 6,message = "Minimum six digits are required")
    private String password;
}

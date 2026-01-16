package com.shivam.aiecommercebackend.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "Username cannot be Empty")
    @NotNull(message = "Username cannot be NULL")
    private String username;

    @NotBlank(message = "Password cannot be Empty")
    @NotNull(message = "Password cannot be NULL")
    @Size(min = 6,message = "Minimum six digits are required")
    private String password;

}

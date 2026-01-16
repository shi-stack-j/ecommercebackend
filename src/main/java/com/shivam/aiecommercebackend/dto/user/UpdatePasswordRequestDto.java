package com.shivam.aiecommercebackend.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequestDto {
    @NotNull(message ="Current password cannot be null")
    @NotBlank(message = "Password is must")
    @Size(min = 6,message = "Minimum six digits are required")
    private String currentPassword;
    @NotNull
    @NotBlank(message = "New Password cannot be null")
    @Size(min = 6,message = "Minimum six digits are required")
    private String newPassword;
}

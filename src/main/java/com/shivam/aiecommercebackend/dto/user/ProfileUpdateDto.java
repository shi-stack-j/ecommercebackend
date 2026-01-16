package com.shivam.aiecommercebackend.dto.user;


import jakarta.validation.constraints.Email;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileUpdateDto {

    private String name;
    @Email(message = "Enter valid email")
    private String email;

}

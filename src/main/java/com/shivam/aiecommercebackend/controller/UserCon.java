package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.user.ProfileUpdateDto;
import com.shivam.aiecommercebackend.dto.user.UpdatePasswordRequestDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.service.UserSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(
        name = "User",
        description = "APIs for managing user profile and account security"
)
public class UserCon {
    @Autowired
    private UserSer userService;

//    Checked
    @PatchMapping("/profile")
    @Operation(
            summary = "Update user profile",
            description = "Allows a user to update their profile details such as name, email or other personal information",
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Username of the user whose profile needs to be updated",
                            required = true,
                            example = "john_doe"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Profile update request payload",
                    content = @Content(
                            schema = @Schema(implementation = ProfileUpdateDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User profile updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid profile data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<UserDto> updateUserProfile(
            @RequestParam String username,
            @Valid @RequestBody ProfileUpdateDto dto
    ) {
        UserDto updatedUser = userService.updateUserProfile(dto, username);
        return ResponseEntity.ok(updatedUser);
    }

//    Checked
    @PutMapping("/update/{username}/password")
    @Operation(
            summary = "Update user password",
            description = "Allows a user to securely change their account password",
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Username of the account",
                            required = true,
                            example = "john_doe"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Password update request payload",
                    content = @Content(
                            schema = @Schema(implementation = UpdatePasswordRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> updatePassword(
            @PathVariable(name = "username")String username,
            @Valid @RequestBody UpdatePasswordRequestDto requestDto
    ){
        ApiResponseDto responseDto=userService.changePassword(username,requestDto);
        return ResponseEntity.ok(responseDto);
    }
}

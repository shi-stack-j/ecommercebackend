package com.shivam.aiecommercebackend.controller;
import com.shivam.aiecommercebackend.dto.auth.LoginRequestDto;
import com.shivam.aiecommercebackend.dto.auth.LoginResponseDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.dto.auth.UserRegisterDto;
import com.shivam.aiecommercebackend.service.AuthSer;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Authentication",description = "APIs for user registration, login, JWT authentication and current user info")
@RequestMapping("/auth")
public class AuthCon {
    @Autowired
    private AuthSer authSer;

    @PostMapping("/user/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided registration details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Register Data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRegisterDto.class))
            ),
//            To make the allow the request without the jwt token
            security = @SecurityRequirement(name = "")
    )
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserRegisterDto registerDto
    ){
        UserDto response=authSer.registerUser(registerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/user/login")
    @Hidden //This will not be shown in swagger ui
    public ResponseEntity<String > loginUser(
            HttpServletRequest request,
            HttpServletResponse responseD,
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ){
        String response=authSer.login(request, responseD, loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/jwt/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT login successful",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Login data ",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequestDto.class) )
            ),
            security = @SecurityRequirement(name = "")
    )
    public ResponseEntity<LoginResponseDto> jwtLogin(
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ){
        LoginResponseDto responseDto=authSer.loginJWT(loginRequestDto);
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/loggedIn/me")
    @Operation(
            summary = "Get current logged-in user",
            description = "Returns information about the currently authenticated user based on JWT/session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))),
            }
    )
    public ResponseEntity<UserDto> getCurrentUser(){
        UserDto response=authSer.loggedInUser();
        return ResponseEntity.ok(response);
    }

}

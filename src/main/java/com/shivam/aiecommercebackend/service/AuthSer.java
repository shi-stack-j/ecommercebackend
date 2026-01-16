package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.auth.LoginRequestDto;
import com.shivam.aiecommercebackend.dto.auth.LoginResponseDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.dto.auth.UserRegisterDto;
import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.mapper.RegisterMap;
import com.shivam.aiecommercebackend.mapper.UserMap;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import com.shivam.aiecommercebackend.utility.JwtAuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthSer {
    @Autowired
    private UserEnRepo userEnRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    @Transactional
    public UserDto registerUser(UserRegisterDto userRegisterDto){
        if(userRegisterDto==null)throw new InvalidInputException("User Register is not valid ");
        if(userRegisterDto.getPassword()==null || userRegisterDto.getPassword().isBlank())throw new InvalidInputException("Password is not valid ");
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        UserEn userEn= RegisterMap.toUserEntity(userRegisterDto);
        userEnRepo.save(userEn);
        return UserMap.toUserDto(userEn);
    }

//    Here i have implemented the session based spring login  using the InMemoryUserDetailsService
    @PreAuthorize("denyAll()")
    public String login(HttpServletRequest request, HttpServletResponse response,LoginRequestDto userLoginDto){
        if (userLoginDto==null)throw new InvalidInputException("User Login request is not valid");
        if(userLoginDto.getPassword()==null || userLoginDto.getPassword().isBlank())throw new InvalidInputException("Password is not valid");
        if(userLoginDto.getUsername()==null || userLoginDto.getUsername().isBlank())throw new InvalidInputException("Username is not valid");
        Authentication authentication=new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),userLoginDto.getPassword());
        Authentication authenticated=authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        SecurityContext context=SecurityContextHolder.getContext();
        securityContextRepository.saveContext(context,request,response);
        return "Login successfully for user :- "+userLoginDto.getUsername();
    }

    public LoginResponseDto loginJWT(LoginRequestDto requestDto){
        if (requestDto==null)throw new InvalidInputException("User Login request is not valid");
        if(requestDto.getPassword()==null || requestDto.getPassword().isBlank())throw new InvalidInputException("Password is not valid");
        if(requestDto.getUsername()==null || requestDto.getUsername().isBlank())throw new InvalidInputException("Username is not valid");
        Authentication authentication=new UsernamePasswordAuthenticationToken(requestDto.getUsername(),requestDto.getPassword());
        Authentication authenticated=authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        UserDetailsPrincipal principal= (UserDetailsPrincipal) authenticated.getPrincipal();
        String token=jwtAuthUtil.generateToken(principal);
        return LoginResponseDto.builder()
                .jwt(token)
                .username(principal.getUsername())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public UserDto loggedInUser(){
        UserDetailsPrincipal userDetailsPrincipal= (UserDetailsPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  UserDto.builder()
                .id(userDetailsPrincipal.getId())
                .name(userDetailsPrincipal.getName())
                .createdAt(userDetailsPrincipal.getCreatedAt())
                .updatedAt(userDetailsPrincipal.getUpdatedAt())
                .email(userDetailsPrincipal.getEmail())
                .isEnabled(userDetailsPrincipal.isEnabled())
                .username(userDetailsPrincipal.getUsername())
                .build();
    }

}

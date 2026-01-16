package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminSerTest {
    @Autowired
    private AdminSer adminSer;
    @Test
    public void updateUserRole(){
        String username="shivamg";
        RoleEnum roleEnum=RoleEnum.ROLE_MANAGER;
        ApiResponseDto responseEntity=adminSer.updateUserRole(username,roleEnum);
        assertEquals(HttpStatus.OK,responseEntity.getStatus());
        assertNotNull(responseEntity.getMessage());
    }
    @Test
    public void getAllUsers(){
        Page<UserDto> responseEntity=adminSer.getAllUsers(0,4);
        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK,responseEntit)/;
    }
//    @Test
//    public void getUser(){
//        String value="shivamg%";
//        String type="username";
//        ResponseEntity<?> responseEntity=adminSer.getUser(value,type);
//        ResponseEntity<?> responseEntity1=adminSer.getUser("6","id");
//        ResponseEntity<?> responseEntity2=adminSer.getUser("sgmail.com","email");
//        assertNotNull(responseEntity.getBody());
//        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//    }
}

package com.shivam.aiecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
public class AuthSerTest {
    @Autowired
    private AuthSer authSer;
//    @Test
//    public void registerUser(){
//        UserRegisterDto userRegisterDto=new UserRegisterDto();
//        userRegisterDto.setName("ShivamGJ");
//        userRegisterDto.setEmail("shivam@gmail.com");
//        userRegisterDto.setUsername("shivamgang");
//        userRegisterDto.setPassword("Shivam@123");
//        ResponseEntity<?> responseEntity=authSer.registerUser(userRegisterDto);
//        assertNotNull(responseEntity);
//        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
//        assertNotNull(responseEntity.getBody());
//    }
}

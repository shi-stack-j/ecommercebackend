package com.shivam.aiecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@Transactional
public class UserSerTest {
    @Autowired
    private UserSer userSer;
//    @Test
//    public void updateUserProfile(){
//        String username="shivam";
//        ProfileUpdateDto profileUpdateDto=new ProfileUpdateDto();
//        profileUpdateDto.setName("OkhBhaiJi");
//        ResponseEntity<?> responseEntity=userSer.updateUserProfile(profileUpdateDto,username);
//        assertNotNull(responseEntity);
//        assertNotNull(responseEntity.getBody());
//        assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
//    }
//    @Test
//    public void changeUserAccountStatus() {
//        String username="shivamg";
//        boolean status=false;
//        ResponseEntity<?> responseEntity=userSer.changeUserAccountStatus(username,status);
//        assertNotNull(responseEntity);
//        assertNotNull(responseEntity.getBody());
//        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
//    }
}

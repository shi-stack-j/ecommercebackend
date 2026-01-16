package com.shivam.aiecommercebackend.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
public class AiSerTest {

    @Autowired
    private AiSer aiSer;
    @Test
    public void getDescription(){
        String query="Provide me the description for the product 'Nothing Phone 3a' also provide the current price";
        String response=aiSer.getDescription(query);
        assertNotNull(response);
        System.out.println(response.length());
        log.info("Description of the product from the model : - "+response);
    }

    @Test
    public void getDescriptionRag(){
        String query="Provide me the description for the product 'Nothing Phone 3a' also provide the current price";
        String response=aiSer.getDescription(query);
        assertNotNull(response);
        System.out.println(response.length());
        log.info("Description of the product from the model : - "+response);
    }
}

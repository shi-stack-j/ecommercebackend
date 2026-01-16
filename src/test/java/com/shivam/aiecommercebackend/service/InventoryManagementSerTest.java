package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryManagementSerTest {
    @Autowired
    private InventoryManagementSer inventoryManagementSer;

    @Test
    public void decreaseQuantity(){
        Long id = 2L;
        int quantity=4;
        ApiResponseDto response=inventoryManagementSer.reduceStock(id,quantity);
        assertNotNull(response);
        assertEquals(response.getStatus(), ApiResponseStatusEnum.SUCCESS);
        assertNotNull(response.getMessage());
    }

    @Test
    public void increaseQuantity(){
        Long id=3L;
        int quantity=10;
        ApiResponseDto response=inventoryManagementSer.increaseStock(id,quantity);
        assertNotNull(response);
        assertEquals(response.getStatus(), ApiResponseStatusEnum.SUCCESS);
        assertNotNull(response.getMessage());

    }

    @Test
    public void changeAvailability(){
        Long id=2L;
        boolean availability=true;
        ApiResponseDto response=inventoryManagementSer.setAvailability(id,availability);
        assertNotNull(response);
        assertEquals(response.getStatus(), ApiResponseStatusEnum.SUCCESS);
        assertNotNull(response.getMessage());
    }
}

package com.shivam.aiecommercebackend.service;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.cart.CartResponseDto;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartSerTest {
    @Autowired
    private  CartSer cartSer;

    @Test
    public void getCard(){
        Long userId=4L;
        CartResponseDto response=cartSer.getCart(userId);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void removeItem(){
        Long userId=1L;
        Long productId=2L;
        CartResponseDto response=cartSer.removeItem(userId,productId);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void addItem(){
        Long userId=1L;
        Long productId=1L;
        Integer quantity=3;
        CartResponseDto response=cartSer.addItem(userId,productId,quantity);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void updateItemQuantity(){
        Long userId=4L;
        Long productId=1L;
        Integer quantity=2;
        CartResponseDto response=cartSer.updateItemQuantity(userId,productId,quantity);
        assertNotNull(response);
        System.out.println(response);
    }

    @Test
    public void clearCart(){
        Long userId=4L;
        ApiResponseDto response=cartSer.clearCart(userId);
        assertNotNull(response);
        assertEquals(response.getStatus(), ApiResponseStatusEnum.SUCCESS);
        assertNotNull(response.getMessage());
    }
}

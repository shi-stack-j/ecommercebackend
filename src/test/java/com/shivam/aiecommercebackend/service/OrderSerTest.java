package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.order.CreateOrderDto;
import com.shivam.aiecommercebackend.dto.order.OrderResponseDto;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderSerTest {

    @Autowired
    private OrderSer orderSer;

    /**
     * Place Order Test
     */
    @Test
    public void placeOrder_success() {
        Long userId = 1L;

        CreateOrderDto dto = new CreateOrderDto();
        dto.setAddress("Rampur, UP");
        dto.setPaymentMode(PaymentMode.COD);

        OrderResponseDto response = orderSer.placeOrder(userId, dto);

        assertNotNull(response);
        assertNotNull(response.getOrderId());
        assertEquals(PaymentMode.COD, response.getPaymentMode());
        assertTrue(response.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(response.getTotalItems() > 0);
    }


    /**
     * Get Order By ID
     */
    @Test
    public void getOrderById_success() {
        Long orderId = 1L;

        OrderResponseDto response = orderSer.getOrderByID(orderId);

        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
    }


    /**
     * Cancel Order
     */
    @Test
    public void cancelOrder_success() {
        Long orderId = 1L;

        OrderResponseDto response = orderSer.cancelOrder(orderId);

        assertNotNull(response);
        assertEquals(OrderStatus.CANCELLED, response.getOrderStatus());
    }


    /**
     * Get Orders of User with Pagination
     */
    @Test
    public void getOrdersOfUser_success() {
        Long userId = 1L;

        Page<OrderResponseDto> response =
                orderSer.getOrderOfUser(userId, 0, 5);

        assertNotNull(response);
        assertTrue(response.getTotalElements() >= 0);
        assertTrue(response.getSize() == 5);
    }


    /**
     * Get All Orders with filters
     */
    @Test
    public void getAllOrders_success() {
        Page<OrderResponseDto> response =
                orderSer.getAllOrders(
                        null,         // paymentMode
                        null,         // fromDate
                        null,         // toDate
                        null,         // status
                        null,         // minAmount
                        null,         // maxAmount
                        0,
                        10
                );

        assertNotNull(response);
        assertTrue(response.getSize() == 10);
    }


    /**
     * Update Order Status
     */
    @Test
    public void updateOrderStatus_success() {
        Long orderId = 1L;

        OrderResponseDto response =
                orderSer.updateOrderStatus(orderId, OrderStatus.SHIPPED);

        assertNotNull(response);
        assertEquals(OrderStatus.SHIPPED, response.getOrderStatus());
        assertNotNull(response.getTrackingNumber());
    }
}

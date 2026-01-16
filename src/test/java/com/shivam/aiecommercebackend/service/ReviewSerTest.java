//package com.shivam.aiecommercebackend.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//public class ReviewSerTest {
//    @Autowired
//    private ReviewSer reviewSer;
//
//    @Test
//    public void addReview(){
//
//    }
//    @Test
//    public void deleteReview(){
//
//    }
//    @Test
//    public void updateReview(){
//
//    }
//    @Test
//    public void getReview(){
//
//    }
//    @Test
//    public void getProductReviews(){
//
//    }
//    @Test
//    public void getUserReviews(){
//
//    }
//}
package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.review.*;
import com.shivam.aiecommercebackend.entity.*;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.repository.OrderEnRepo;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import com.shivam.aiecommercebackend.repository.ReviewEnRepo;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewSerTest {

    @Autowired
    private ReviewSer reviewSer;

    @Autowired
    private UserEnRepo userRepo;

    @Autowired
    private ProductEnRepo productRepo;

    @Autowired
    private OrderEnRepo orderRepo;

    @Autowired
    private ReviewEnRepo reviewRepo;

    // ---------- Helper Methods ----------

    private UserEn createUser() {
        UserEn user = new UserEn();
        user.setEmail("test@gmail.com");
        user.setName("Test User");
        user.setUsername("Hello");
        return userRepo.save(user);
    }

    private ProductEn createProduct() {
        ProductEn product = new ProductEn();
        product.setName("iPhone 15");
        product.setPrice(BigDecimal.valueOf(100000));
        product.setQuantity(10);
        product.setSku("SKU12345");
        return productRepo.save(product);
    }

    private void createDeliveredOrder(UserEn user, ProductEn product) {
        OrderEn order = new OrderEn();
        order.setUserEn(user);
        order.setOrderStatus(OrderStatus.DELIVERED);

        OrderItem item = new OrderItem();
        item.setProductEn(product);
        item.setQuantity(1);
        item.setOrderEn(order);
        item.setPrice(BigDecimal.valueOf(134));

        order.setTotalAmount(BigDecimal.ZERO);
        order.setItems(List.of(item));
        order.setAddress("Up,Bareilly");
        orderRepo.save(order);
    }

    // ---------- TESTS ----------

    @Test
    void addReview_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(5);
        dto.setDescription("Excellent product");

        ReviewDto review = reviewSer.addReview(dto, product.getId());

        assertNotNull(review);
        assertEquals(5, review.getRating());
        assertEquals("Excellent product", review.getDescription());
    }

    @Test
    void updateReview_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(4);
        dto.setDescription("GoodOKLPKMMKM");

        ReviewDto saved = reviewSer.addReview(dto, product.getId());

        ReviewUpdateRequestDto update = new ReviewUpdateRequestDto();
        update.setRating(5);
        update.setDescription("Excellent");

        ReviewDto updated = reviewSer.updateReview(saved.getId(), update);

        assertEquals(5, updated.getRating());
        assertEquals("Excellent", updated.getDescription());
    }

    @Test
    void deleteReview_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(3);
        dto.setDescription("Average");

        ReviewDto saved = reviewSer.addReview(dto, product.getId());

        reviewSer.deleteReview(saved.getId());

        assertFalse(reviewRepo.existsById(saved.getId()));
    }

    @Test
    void getProductReviews_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(5);
        dto.setDescription("Nicmjnme");

        reviewSer.addReview(dto,  product.getId());

        var page = reviewSer.getProductReviews(product.getId(), 0, 10);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void getUserReviews_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(4);
        dto.setDescription("Goodmnjb");

        reviewSer.addReview(dto,  product.getId());

        var page = reviewSer.getUserReview(user.getId(), 0, 10);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void getAverageRating_success() {
        UserEn user = createUser();
        ProductEn product = createProduct();
        createDeliveredOrder(user, product);

        ProductReviewRequestDto dto = new ProductReviewRequestDto();
        dto.setRating(4);
        dto.setDescription("Nicemnjh");

        reviewSer.addReview(dto,  product.getId());

        Double avg = reviewSer.getAverageRating(product.getId());

        assertEquals(4.0, avg);
    }
}

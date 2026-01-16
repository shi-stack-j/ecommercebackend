package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.product.ProductCreateDto;
import com.shivam.aiecommercebackend.dto.product.ProductResponseDto;
import com.shivam.aiecommercebackend.dto.product.ProductUpdateRequestDto;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.mapper.ProductMap;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@SpringBootTest
public class ProductSerTest {
    private static final Logger log = LoggerFactory.getLogger(ProductSerTest.class);
    @Autowired
    private ProductSer productSer;

    @Test
    public void createProduct() {
        ProductCreateDto productCreateDto = ProductCreateDto
                .builder()
                .name("iPhone 13")//iPhone 13pro
                .description("This is the iPhone 13 pro")
                .price(BigDecimal.valueOf(100000))
                .discount(BigDecimal.valueOf(35))
                .quantity(4)
                .build();
        String categoryName = "Electronics";

        ApiResponseDto responseDto = productSer.createProduct(productCreateDto, categoryName);
        assertNotNull(responseDto);
        assertEquals(ApiResponseStatusEnum.SUCCESS, responseDto.getStatus());
        assertNotNull(responseDto.getMessage());
    }
    @Test
    public void getProductById(){
        Long id=1L;
        ProductResponseDto response=productSer.getProduct(id);
        assertNotNull(response);
        System.out.println(response);
    }
    @Test
    public void getAllProducts(){
        int page=1,size=1;
        Page<ProductResponseDto> response=productSer.getAllProducts(page,size);
        assertNotNull(response);
        assertNotNull(response.getContent());

    }
    @Test
    public void updateProduct(){
        Long id=1L;
        ProductUpdateRequestDto updateRequestDto=new ProductUpdateRequestDto();
        updateRequestDto.setDescription("Description is updates");
        ApiResponseDto responseDto=productSer.updateProduct(id,updateRequestDto);
        assertNotNull(responseDto);
        assertEquals(responseDto.getStatus(),ApiResponseStatusEnum.SUCCESS);
        assertNotNull(responseDto.getMessage());
    }
    @Test
    public void deleteProduct(){
        Long id=1L;
        ApiResponseDto responseDto=productSer.deleteProduct(id);
        assertNotNull(responseDto);
        assertEquals(responseDto.getStatus(),ApiResponseStatusEnum.SUCCESS);
        assertNotNull(responseDto.getMessage());
    }
    @Test
    public void getProductsByCategory(){
        String categoryName="Electronic";
        Integer page=0;
        Integer size=5;
        Page<ProductResponseDto> response=productSer.getProductsByCategory(categoryName,page,size);
        assertNotNull(response);
        assertNotNull(response.getContent());
    }
    @Test
    public void searchProducts(){
        String keyword="iPhone";
        Integer page=0;
        Integer size=5;
        Page<ProductResponseDto> response=productSer.searchProducts(keyword,page,size);
        assertNotNull(response);
        assertNotNull(response.getContent());
    }
    @Test
    public void updateProductPrice( ){
        Long id=1L;
        BigDecimal newPrice=BigDecimal.valueOf(32000);
        ApiResponseDto responseDto=productSer.updateProductPrice(id,newPrice);
        assertNotNull(responseDto);
        assertEquals(responseDto.getStatus(),ApiResponseStatusEnum.SUCCESS);
        assertNotNull(responseDto.getMessage());
    }
    @Test
    public void updateProductDiscount(){
        Long id=1L;
        BigDecimal newDiscount=BigDecimal.valueOf(50);
        ApiResponseDto responseDto=productSer.updateProductDiscount(id,newDiscount);
        assertNotNull(responseDto);
        assertEquals(responseDto.getStatus(),ApiResponseStatusEnum.SUCCESS);
        assertNotNull(responseDto.getMessage());
    }
    @Test
    public void updateProductDescriptionWithAi(){
        Long productId=5L;
        ApiResponseDto responseDto=productSer.updateProductDescriptionWithAi(productId);
        assertNotNull(responseDto);
        assertEquals(ApiResponseStatusEnum.SUCCESS,responseDto.getStatus());
        log.info("Response message is :- "+responseDto.getMessage());

    }
}

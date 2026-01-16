package com.shivam.aiecommercebackend.service;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryDto;
import com.shivam.aiecommercebackend.dto.category.CategoryResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryUpdateRequestDto;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class CategorySerTest {
    @Autowired
    private CategorySer categorySer;

    @Test
    public void createCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Mobiles");
        categoryDto.setDescription("Hello this is Description of Mobiles Category");
        ApiResponseDto response = categorySer.createCategory(categoryDto);
        assertNotNull(response);
        assertEquals(ApiResponseStatusEnum.SUCCESS, response.getStatus());
        assertNotNull(response.getMessage());
    }
    @Test
    public void getCategory(){
        String type="name";
        String value="electronics";
        CategoryResponseDto response=categorySer.getCategory(value,type);
        assertNotNull(response);
        System.out.println("Category name is :- "+response.getName());

    }
    @Test
    public void removeCategory(){
        Long id=1L;
        ApiResponseDto response=categorySer.removeCategory(id);
        assertNotNull(response);
        assertEquals(response.getStatus(),ApiResponseStatusEnum.SUCCESS);
        assertNotNull(response.getMessage());
    }
    @Test
    public void updateCategory(){
        Long id=2L;
        CategoryResponseDto categoryResponseDto=categorySer.updateCategory(
                id,
                CategoryUpdateRequestDto.builder()
                        .name("Electronics")
                        .description("")
                        .build()
                );
        assertNotNull(categoryResponseDto);
        System.out.println(categoryResponseDto.getDescription());
    }
}

package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.category.CategoryDto;
import com.shivam.aiecommercebackend.dto.category.CategoryResponseDto;
import com.shivam.aiecommercebackend.entity.CategoryEn;
import com.shivam.aiecommercebackend.exception.InvalidInputException;

public final class CategoryMap {
    public static CategoryEn toCategoryEntity(CategoryDto categoryDto){
        CategoryEn categoryEn=new CategoryEn();
        categoryEn.setName(categoryDto.getName());
        categoryEn.setDescription(categoryDto.getDescription());
//        categoryEn.setPolicyEn(categoryDt);
        return categoryEn;
    }
    public static CategoryResponseDto toResponseDto(CategoryEn categoryEn){
//        Instead of this we should use builder pattern it is industry ready
//        CategoryResponseDto categoryResponseDto=new CategoryResponseDto();
//        categoryResponseDto.setName(categoryEn.getName());
//        categoryResponseDto.setDescription(categoryEn.getDescription());
//        categoryResponseDto.setCreatedAt(categoryEn.getCreatedAt());
//        Here we will use the builder pattern
        return CategoryResponseDto.builder()
                .id(categoryEn.getId())
                .name(categoryEn.getName())
                .description(categoryEn.getDescription())
                .createdAt(categoryEn.getCreatedAt())
                .policyId(categoryEn.getPolicyEn().getId())
                .build();
    }
}

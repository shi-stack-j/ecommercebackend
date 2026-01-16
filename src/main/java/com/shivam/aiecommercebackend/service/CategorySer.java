package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryDto;
import com.shivam.aiecommercebackend.dto.category.CategoryResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryUpdateRequestDto;
import com.shivam.aiecommercebackend.entity.CategoryEn;
import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.CategoryMap;
import com.shivam.aiecommercebackend.repository.CategoryEnRepository;
import com.shivam.aiecommercebackend.repository.ReturnPolicyRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategorySer {
    @Autowired
    private CategoryEnRepository categoryEnRepository;
    @Autowired
    private ReturnPolicyRepo returnPolicyRepo;
//    Create Category
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto createCategory(CategoryDto categoryDto){
        if (categoryDto==null || categoryDto.getPolicyId()==null || categoryDto.getName().isBlank() || categoryDto.getName().trim().isEmpty()){
            throw new InvalidInputException("Enter valid input");
        }
            ReturnPolicyEn returnPolicy=returnPolicyRepo.findById(categoryDto.getPolicyId())
                    .orElseThrow(()->new ResourceNotFoundException("Policy not found with the given id :- "+categoryDto.getPolicyId()));
            CategoryEn categoryEn=CategoryMap.toCategoryEntity(categoryDto);
            categoryEn.setPolicyEn(returnPolicy);
            categoryEnRepository.save(categoryEn);
            return ApiResponseDto.builder()
                    .status(ApiResponseStatusEnum.SUCCESS)
                    .message("Category Created Successfully")
                    .time(LocalDateTime.now())
                    .build();
    }
    //    Get Category
//    Improvement needed
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public CategoryResponseDto getCategory(String value,String type){
        if( value==null || value.isBlank() || type==null || type.isBlank()){
            throw new InvalidInputException("Enter valid Value And Type");
        }
        value=value.trim().toLowerCase();
        type=type.trim().toLowerCase();
        CategoryEn categoryEn;
        String message;
        switch (type){
            case "id":
                if(!value.matches("\\d+")) throw new InvalidInputException("Id should be numbers only");
                message="Category not found with the given  ID :- "+value;
                categoryEn=categoryEnRepository.findById(Long.valueOf(value))
                        .orElseThrow(()->new ResourceNotFoundException(message));
                break;
            case "name":
                message="Category not found with the given  Name :- "+value;
                categoryEn=categoryEnRepository.findByNameIgnoreCase(value)
                        .orElseThrow(()->new ResourceNotFoundException(message));
                break;
            default:
                throw new InvalidInputException("Invalid type. Allowed values: id, name");
        }
        return CategoryMap.toResponseDto(categoryEn);
    }
//    Update Category
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto categoryUpdateRequestDto){
        if(id==null || id<=0)throw  new InvalidInputException("ID must be valid");
        if(categoryUpdateRequestDto==null) throw new InvalidInputException("Provide valid CategoryUpdateDTo");
        boolean isNameInvalid=categoryUpdateRequestDto.getName()==null || categoryUpdateRequestDto.getName().isBlank();
        boolean isDescriptionInvalid=categoryUpdateRequestDto.getDescription()==null || categoryUpdateRequestDto.getDescription().isBlank();
        if(isDescriptionInvalid && isNameInvalid){
            throw new InvalidInputException("At least one field (name or description) must be provided for update");
        }
        CategoryEn categoryEn=categoryEnRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with the given id :- "+id));
        if(!isDescriptionInvalid && !Objects.equals(categoryEn.getDescription(),categoryUpdateRequestDto.getDescription().trim())) categoryEn.setDescription(categoryUpdateRequestDto.getDescription().trim());
        if(!isNameInvalid && !Objects.equals(categoryEn.getName(),categoryUpdateRequestDto.getName().trim())) categoryEn.setName(categoryUpdateRequestDto.getName().trim());
        return CategoryMap.toResponseDto(categoryEn);
    }
//    Delete Category
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ApiResponseDto removeCategory(Long id){
        if(id==null || id<=0)throw new InvalidInputException("Id is INVALID. Provide valid id...");
        categoryEnRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with the given id :- "+id));
        categoryEnRepository.deleteById(id);
        return ApiResponseDto.builder().message("Category Removed Successfully").status(ApiResponseStatusEnum.SUCCESS).time(LocalDateTime.now()).build();
    }
//    Update Policy
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public CategoryResponseDto updatePolicy(Long categoryId,Long policyId){
        if(policyId== null || policyId<=0)throw new InvalidInputException("Policy Id is not valid ");
        if(categoryId==null || categoryId<=0)throw new InvalidInputException("Category id is not valid ");
        CategoryEn categoryEn=categoryEnRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with the given id :- "+categoryId));
        if(categoryEn.getPolicyEn().getId().equals(policyId)){
            return CategoryMap.toResponseDto(categoryEn);
        }
        ReturnPolicyEn policyEn=returnPolicyRepo.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy not found with the given id :- "+policyId));
        categoryEn.setPolicyEn(policyEn);
        CategoryEn savedCategory=categoryEnRepository.save(categoryEn);
        return CategoryMap.toResponseDto(savedCategory);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Page<CategoryResponseDto> getAllCategories(Integer page,Integer size){
        if(page==null || page<0)throw new InvalidInputException("Page number is not valid");
        if(size==null || size<=0 || size>100) throw new InvalidInputException("Size is not valid [ constraint :- 0<=size>100 ]");
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt"));
        Page<CategoryEn> categories=categoryEnRepository.findAll(pageable);
        return categories.map(CategoryMap::toResponseDto);
    }
}

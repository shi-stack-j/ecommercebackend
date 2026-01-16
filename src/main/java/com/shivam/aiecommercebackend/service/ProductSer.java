package com.shivam.aiecommercebackend.service;

import com.cloudinary.Api;
import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.CloudinaryUploadResult;
import com.shivam.aiecommercebackend.dto.product.ProductCreateDto;
import com.shivam.aiecommercebackend.dto.product.ProductResponseDto;
import com.shivam.aiecommercebackend.dto.product.ProductUpdateRequestDto;
import com.shivam.aiecommercebackend.entity.CategoryEn;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.ProductMap;
import com.shivam.aiecommercebackend.repository.CategoryEnRepository;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
public class ProductSer {
    private static final Logger log = LoggerFactory.getLogger(ProductSer.class);
    @Autowired
    private CategoryEnRepository categoryRepo;
    @Autowired
    private ProductEnRepo productEnRepo;
    @Autowired
    private CloudinarySer cloudinarySer;
    @Autowired
    private AiSer aiSer;
//    Create Product
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto createProduct(ProductCreateDto productCreateDto,String categoryName){
        if(productCreateDto==null){
            throw new InvalidInputException("Enter valid DTO");
        }
        if(categoryName==null || categoryName.isBlank())throw new InvalidInputException("Category name is not valid");
        CategoryEn categoryEn=categoryRepo.findByNameIgnoreCase(categoryName.trim())
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with the given name :- "+categoryName));
        ProductEn productEn= ProductMap.toProductEntity(productCreateDto,categoryEn);
        productEnRepo.save(productEn);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .message("Product Added Successfully")
                .build();
    }
//    Get Product by ID
    @PreAuthorize("isAuthenticated()")
    public ProductResponseDto getProduct(Long id){
        if(id==null || id<=0)throw new InvalidInputException("Product id must be valid");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMap.toResponseDto(productEn);
    }

//    Get All Products (Pagination)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Page<ProductResponseDto> getAllProducts(int page , int size){
        if(page<0 )throw new InvalidInputException("Page number should be positive or zero");
        if(size<=0) throw new InvalidInputException("Size of page must be greater then zero");
        if(size>100)throw new InvalidInputException("Size limit exceeds size could not be greater then 100 ");
        Pageable request=PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<ProductEn> products=productEnRepo.findAll(request);
        return products.map(ProductMap::toResponseDto);
    }

//    Update Product (Partial – PATCH)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto updateProduct(Long id, ProductUpdateRequestDto updateDto){
        if(id==null || id<=0)throw new InvalidInputException("Id is invalid Provide the valid id");
        if(updateDto==null)throw new InvalidInputException("Provide valid update DTO");
        boolean isNameInValid=updateDto.getName()==null ||updateDto.getName().isBlank() ;
        boolean isDescriptionInValid=updateDto.getDescription()==null || updateDto.getDescription().isBlank();
        if (isDescriptionInValid && isNameInValid)throw new InvalidInputException("At least one field (name or description) must be provided for update");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));
        if(!isNameInValid && !Objects.equals(productEn.getName(),updateDto.getName().trim()))  productEn.setName(updateDto.getName().trim());
        if(!isDescriptionInValid && !Objects.equals(productEn.getDescription(),updateDto.getDescription().trim())) productEn.setDescription(updateDto.getDescription().trim());
        return ApiResponseDto.builder()
                .message("Product updated successfully")
                .status(ApiResponseStatusEnum.SUCCESS)
                .time(LocalDateTime.now())
                .build();
    }
//    Delete Product
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ApiResponseDto deleteProduct(Long id){
        if(id==null || id<=0)throw new InvalidInputException("Enter valid id ");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));
        productEnRepo.delete(productEn);
        return ApiResponseDto.builder()
                .status(ApiResponseStatusEnum.SUCCESS)
                .time(LocalDateTime.now())
                .message("Product Deleted Successfully...")
                .build();
    }

    @PreAuthorize("isAuthenticated()")
//    Get Products by Category
    public Page<ProductResponseDto> getProductsByCategory(String categoryName,Integer page , Integer size){
        if(categoryName==null || categoryName.isBlank())throw new InvalidInputException("Provide valid Category Name");
        if(page<0 )throw new InvalidInputException("Page number should be positive or zero");
        if(size<=0) throw new InvalidInputException("Size of page must be greater then zero");
        if(size>100)throw new InvalidInputException("Size limit exceeds size could not be greater then 100 ");
        Pageable request=PageRequest.of(page,size,Sort.by("createdAt").descending());
        Page<ProductEn> products=productEnRepo.findByCategoryEn_NameIgnoreCase(categoryName.trim(),request);
        return products.map(ProductMap::toResponseDto);
    }
    @PreAuthorize("isAuthenticated()")
//    Search Products
    public Page<ProductResponseDto> searchProducts(String keyword,Integer page ,Integer size){
        if(keyword==null || keyword.isBlank()) throw new InvalidInputException("Keyword cannot be empty or null");
        if(page<0 )throw new InvalidInputException("Page number should be positive or zero");
        if(size<=0) throw new InvalidInputException("Size of page must be greater then zero");
        if(size>100)throw new InvalidInputException("Size limit exceeds size could not be greater then 100 ");
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdAt").descending());
        String searchKey=keyword.trim();
        Page<ProductEn> products=productEnRepo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchKey,searchKey,pageable);
        Page<ProductResponseDto> productEns=products.map(ProductMap::toResponseDto);
        return products.map(ProductMap::toResponseDto);
    }
//    Stock Management :- Created the Separate Service for that
//    Price & Discount Calculation
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto updateProductPrice(Long id, BigDecimal newPrice){
        if(id==null || id<=0)throw new InvalidInputException("Id is invalid. Provide valid id");
        if(newPrice==null || newPrice.compareTo(BigDecimal.ZERO)<=0 )throw new InvalidInputException("Provide the valid Price");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new InvalidInputException("Product not found with the given id :- +"+id));
        if(!(newPrice.compareTo(productEn.getPrice())==0))productEn.setPrice(newPrice);
        return ApiResponseDto.builder()
                .message("Price updates successfully")
                .status(ApiResponseStatusEnum.SUCCESS)
                .time(LocalDateTime.now())
                .build();
    }
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto updateProductDiscount(Long id,BigDecimal newDiscount){
        if(id==null || id<=0)throw new InvalidInputException("Id is invalid. Provide valid id");
        if(newDiscount==null || newDiscount.compareTo(BigDecimal.ZERO)<=0 || newDiscount.compareTo(BigDecimal.valueOf(100))>0 )throw new InvalidInputException("Provide the Valid value of discount b/w 0 to 100");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));
        if(!(newDiscount.compareTo(productEn.getDiscount())==0))productEn.setDiscount(newDiscount);
        return ApiResponseDto.builder()
                .message("Discount updates Successfully")
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }
//    Product Reviews Summary

//    Update Product description with ai
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto updateProductDescriptionWithAi(Long productId){
        if(productId==null || productId<=0)throw new InvalidInputException("Product id is not valid ");
        ProductEn productEn=productEnRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id...."));
        String rewrittenDescription= aiSer.rewriteDescription(productEn.getName(),productEn.getCategoryEn().getName(),productEn.getDescription());
        System.out.println("New description is :- "+rewrittenDescription);
        productEn.setDescription(rewrittenDescription);
        return ApiResponseDto.builder()
                .message("Description updated Successfully..")
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto uploadProductImage(Long productId, MultipartFile file){
        if(productId==null || productId<=0)throw new  InvalidInputException("Product is not valid");
        if(file==null ||file.isEmpty())throw new InvalidInputException("File is not correct ");
        if (!Set.of("image/jpeg", "image/png", "image/webp")
                .contains(file.getContentType()))
            throw new InvalidInputException("Only JPEG, PNG, WEBP allowed");

        if (file.getSize() > 5 * 1024 * 1024)
            throw new InvalidInputException("Image size must be <= 5MB");

        ProductEn productEn=productEnRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id....."));

        if(productEn.getPublicId()!=null && !productEn.getPublicId().isBlank())cloudinarySer.deleteImage(productEn.getPublicId());

        CloudinaryUploadResult url=cloudinarySer.uploadImage(file);
        productEn.setImageUrl(url.getSecureUrl());
        productEn.setPublicId(url.getPublicId());
        return ApiResponseDto.builder()
                .status(ApiResponseStatusEnum.SUCCESS)
                .message("Images uploaded successfully")
                .time(LocalDateTime.now())
                .build();
    }


}


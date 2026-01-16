package com.shivam.aiecommercebackend.mapper;

import com.shivam.aiecommercebackend.dto.product.ProductCreateDto;
import com.shivam.aiecommercebackend.dto.product.ProductResponseDto;
import com.shivam.aiecommercebackend.entity.CategoryEn;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.entity.ReturnPolicyEn;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.utility.PriceCalculator;
import com.shivam.aiecommercebackend.utility.SkuGenerator;

import java.math.BigDecimal;

public final class ProductMap {
    public static ProductEn toProductEntity(ProductCreateDto productCreateDto, CategoryEn categoryEn){
        if(productCreateDto==null || categoryEn==null){
            throw new InvalidInputException("Provide valid Category Entity and Product Dto");
        }
        if (productCreateDto.getDiscount() != null &&
                productCreateDto.getDiscount().compareTo(productCreateDto.getPrice()) > 0) {
            throw new InvalidInputException("Discount cannot be greater than price");
        }
        ProductEn productEn=new ProductEn();
        productEn.setName(productCreateDto.getName());
        productEn.setDescription(productCreateDto.getDescription());
        productEn.setDiscount(productCreateDto.getDiscount());
        productEn.setQuantity(productCreateDto.getQuantity());
        productEn.setPrice(productCreateDto.getPrice());
        if(productCreateDto.getAvailability()!=null )productEn.setAvailability(productCreateDto.getAvailability());
        productEn.setCategoryEn(categoryEn);
        String sku= SkuGenerator.generate(productCreateDto.getName(),categoryEn.getName());
        productEn.setSku(sku);
        return productEn;
    }
    public static ProductResponseDto toResponseDto(ProductEn productEn){
        if(productEn==null)throw new InvalidInputException("Product entity cannot be null");
        BigDecimal priceAfterDiscount= PriceCalculator.calculateDiscountedPrice(productEn.getPrice(),productEn.getDiscount());
        ReturnPolicyEn policy = productEn.getCategoryEn() != null ? productEn.getCategoryEn().getPolicyEn() : null;
        Integer windowSize = policy != null ? policy.getWindowSize() : 0;
        String capabilities = policy != null ? policy.getCapabilities().toString() : "NONE";
        String imageUrl=productEn.getImageUrl()!=null ? productEn.getImageUrl() : "NONE";

        return ProductResponseDto.builder()
                .windowSize(windowSize)
                .returnCapabilities(capabilities)
                .id(productEn.getId())
                .name(productEn.getName())
                .availability(productEn.getAvailability())
                .description(productEn.getDescription())
                .discount(productEn.getDiscount())
                .originalPrice(productEn.getPrice())
                .createdAt(productEn.getCreatedAt())
                .updatedAt(productEn.getUpdatedAt())
                .sku(productEn.getSku())
                .discountPrice(priceAfterDiscount)
                .quantity(productEn.getQuantity())
                .imageUrl(imageUrl)
                .build();
    }
}

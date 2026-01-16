package com.shivam.aiecommercebackend.service;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.exception.InSufficientStockException;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryManagementSer {
    private static final int LOW_STOCK_THRESHOLD=5;
    @Autowired
    private ProductEnRepo productEnRepo;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto reduceStock(Long id, Integer quantity){
        if(id==null)throw new InvalidInputException("Enter valid id");
        if(quantity==null)quantity=1;
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));

        if(productEn.getQuantity()<quantity){
            throw new InSufficientStockException("Not enough stock");
        }
        Integer updatesStock=productEn.getQuantity()-quantity;
        productEn.setQuantity(updatesStock);
        productEn.setAvailability(updatesStock>0);
        if(updatesStock<LOW_STOCK_THRESHOLD)LowStockAlert(productEn);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .message("Stock reduced Successfully")
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto increaseStock(Long id,Integer quantity){
        if(id==null)throw new InvalidInputException("Enter valid id");
        if(quantity==null)quantity=1;
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));
        int updatedStock=productEn.getQuantity()+quantity;
        productEn.setQuantity(updatedStock);
        productEn.setAvailability(true);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .message("Stock increased Successfully")
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Transactional
    public ApiResponseDto setAvailability(Long id,Boolean newAvailability){
        if(id==null)throw new InvalidInputException("Enter valid id");
        if(newAvailability==null)throw new InvalidInputException("Provide the correct availability");
        ProductEn productEn=productEnRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+id));
        if(productEn.getAvailability()!=newAvailability)productEn.setAvailability(newAvailability);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .message("Stock Availability Successfully")
                .status(ApiResponseStatusEnum.SUCCESS)
                .build();
    }

    private void LowStockAlert(ProductEn product){
        System.out.println("LOW STOCK ALERT for product: " + product.getName());
    }
}

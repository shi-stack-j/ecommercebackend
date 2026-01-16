package com.shivam.aiecommercebackend.service;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.cart.CartResponseDto;
import com.shivam.aiecommercebackend.entity.CartEn;
import com.shivam.aiecommercebackend.entity.CartItem;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.exception.InSufficientStockException;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.CartMap;
import com.shivam.aiecommercebackend.repository.CartItemRepo;
import com.shivam.aiecommercebackend.repository.CartRepo;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import com.shivam.aiecommercebackend.utility.PriceCalculator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CartSer {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private UserEnRepo userRepo;
    @Autowired
    private ProductEnRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #userId==principal.id")
    public CartResponseDto getCart(Long userId){
        if(userId==null || userId<=0)throw new InvalidInputException("Provide valid userId");
        UserEn userEn=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given id :- "+userId));
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseGet(()->{
                    CartEn cartE=new CartEn();
                    cartE.setUserEn(userEn);
                    cartE.setTotalAmount(BigDecimal.ZERO);
                    return cartRepo.save(cartE);
                });
        return CartMap.toCartResponse(cartEn);
    }

    @Transactional
    @PreAuthorize("#userId==principal.id")
    public CartResponseDto addItem(Long userId, Long productId, Integer quantity){
        if(userId==null || userId<=0)throw new InvalidInputException("User id Cannot be Zero or Null");
        if(productId==null || productId<=0)throw new InvalidInputException("Product id Cannot be Zero or Null");
        quantity=quantity==null?1:quantity;
        if( quantity<=0)throw new InvalidInputException("Enter valid quantity");

        UserEn userEn=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the given id :- "+userId));
        ProductEn productEn=productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found with the given id :- "+productId));

        if(!productEn.getAvailability() || productEn.getQuantity()<quantity) throw new InSufficientStockException("Product is not available ");
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseGet(()->{
                    CartEn cartE=new CartEn();
                    cartE.setTotalAmount(BigDecimal.ZERO);
                    cartE.setUserEn(userEn);
                    return cartRepo.save(cartE);
                });
        CartItem item=cartEn.getItems().get(productId);

        if(item==null){
            CartItem cartItem=new CartItem();
            BigDecimal finalPrice =
                    PriceCalculator.calculateDiscountedPrice(
                            productEn.getPrice(),
                            productEn.getDiscount()
                    );
            cartItem.setPrice(finalPrice);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cartEn);
            cartEn.getItems().put(productId, cartItem);
            cartItem.setProductEn(productEn);
//            cartItemRepo.save(cartItem);
        }else{
            item.setQuantity(item.getQuantity()+ quantity);
        }
        cartEn.calculateTotal();
        return CartMap.toCartResponse(cartRepo.save(cartEn));
    }

    @PreAuthorize("#userId == principal.id")
    @Transactional
    public CartResponseDto updateItemQuantity(Long userId, Long productId, Integer quantity){
        if(userId==null || userId<=0)throw new InvalidInputException("User id Cannot be Zero or Null");
        if(productId==null || productId<=0)throw new InvalidInputException("Product id Cannot be Zero or Null");
        if(quantity==null || quantity<0)throw new InvalidInputException("Provide valid Quantity");
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found for the given UserID :- "+userId));
        CartItem cartItem=cartEn.getItems().get(productId);
        if(cartItem==null)throw new ResourceNotFoundException("Product not found for the given id :- "+productId);
        if(cartItem.getProductEn().getQuantity()<quantity)throw new InSufficientStockException("Insufficient stock ");
        if(quantity==0){
            cartEn.getItems().remove(productId);
        }else{
            cartItem.setQuantity(quantity);
            cartEn.getItems().put(productId,cartItem);
        }
        cartEn.calculateTotal();
        cartRepo.save(cartEn);
        return CartMap.toCartResponse(cartEn);
    }

    @PreAuthorize("#userId == principal.id")
    @Transactional
    public CartResponseDto removeItem(Long userId, Long productId){
        if(userId==null || userId<=0)throw new InvalidInputException("Provide valid UserId it cannot be NULL or less then or Zero");
        if(productId==null || productId<=0)throw new InvalidInputException("Provide valid ProductId it cannot be NULL or Less then equals to zero");
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found for the given user... "+userId));
        CartItem item=cartEn.getItems().get(productId);
        if(item==null)throw new ResourceNotFoundException("Product not found in the Cart for the given id :- "+productId);
        cartEn.getItems().remove(productId);
        cartEn.calculateTotal();
        cartRepo.save(cartEn);
        return CartMap.toCartResponse(cartEn);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id ")
    @Transactional
    public ApiResponseDto clearCart(Long userId){
        if(userId==null || userId<=0)throw new InvalidInputException("Provide valid input. UserId cannot be NULL or ZERO");
        CartEn cartEn=cartRepo.findByUserEn_Id(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found for the given userId :- "+userId));
        if(cartEn.getItems().isEmpty())throw new InvalidInputException("Cart is already empty");
        cartEn.getItems().clear();
        cartEn.setTotalAmount(BigDecimal.ZERO);
        cartRepo.save(cartEn);
        return ApiResponseDto.builder()
                .status(ApiResponseStatusEnum.SUCCESS)
                .message("Cart Cleared Successfully...")
                .time(LocalDateTime.now())
                .build();
    }
}

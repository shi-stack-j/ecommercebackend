package com.shivam.aiecommercebackend.mapper;


import com.shivam.aiecommercebackend.dto.cart.CartItemResponseDto;
import com.shivam.aiecommercebackend.dto.cart.CartResponseDto;
import com.shivam.aiecommercebackend.entity.CartEn;
import com.shivam.aiecommercebackend.entity.CartItem;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.utility.PriceCalculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class CartMap {
    public static CartResponseDto toCartResponse(CartEn cartEn){
        CartResponseDto cartResponseDto=new CartResponseDto();
        cartResponseDto.setId(cartEn.getId());
        cartResponseDto.setCreatedAt(cartEn.getCreatedAt());
        cartResponseDto.setUpdatedAt(cartEn.getUpdatedAt());
        cartResponseDto.setTotalAmount(cartEn.getTotalAmount());
        if(cartEn.getItems().isEmpty()){
            cartResponseDto.setIsEmpty(true);
            cartResponseDto.setTotalItems(0);
            cartResponseDto.setProducts(List.of());
        }else{
            List<CartItemResponseDto> response=
                    cartEn.getItems()
                            .values()
                            .stream()
                            .map(CartMap::toCartItemResponseDto)
                            .toList();
            int totalItems =
                    cartEn.getItems()
                            .values()
                            .stream()
                            .mapToInt(CartItem::getQuantity)
                            .sum();
            cartResponseDto.setIsEmpty(false);
            cartResponseDto.setTotalItems(totalItems);
            cartResponseDto.setProducts(response);
        }
        return cartResponseDto;
    }
    private static CartItemResponseDto toCartItemResponseDto(CartItem cartItem){
        BigDecimal price=cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        return CartItemResponseDto.builder()
                .price(cartItem.getPrice())
                .name(cartItem.getProductEn().getName())
                .productId(cartItem.getProductEn().getId())
                .availability(cartItem.getProductEn().getAvailability())
                .subTotal(price)
                .quantity(cartItem.getQuantity())
                .build();
    }
}

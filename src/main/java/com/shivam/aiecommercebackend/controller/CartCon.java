package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.cart.CartResponseDto;
import com.shivam.aiecommercebackend.service.CartSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
@Tag(
        name = "Cart",
        description = "APIs for managing user shopping cart such as adding, updating and removing items"
)
public class CartCon {
    @Autowired
    private CartSer cartSer;

//    Checked
    @GetMapping("/user/{userId}/cart")
    @Operation(
            summary = "Get user cart",
            description = "Fetches the current cart details of a user using user ID",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose cart needs to be retrieved",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CartResponseDto> getCart(
            @PathVariable Long userId
    ){
        CartResponseDto response=cartSer.getCart(userId);
        return ResponseEntity.ok(response);
    }

//    Checked
    @PutMapping("/users/{userId}/cart/items")
    @Operation(
            summary = "Add item to cart",
            description = "Adds a product to user's cart with specified quantity",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", required = true),
                    @Parameter(name = "productId", description = "Product ID to add", required = true),
                    @Parameter(name = "quantity", description = "Quantity of product", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item added to cart successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CartResponseDto>  addItemToCart(
            @PathVariable Long userId,
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "quantity") Integer quantity
    ){
        CartResponseDto response=cartSer.addItem(userId,productId,quantity);
        return ResponseEntity.ok(response);
    }


//    Checked
    @PutMapping("/{userId}/cart/items/{productId}")
    @Operation(
            summary = "Update cart item quantity",
            description = "Updates quantity of a specific product in user's cart",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", required = true),
                    @Parameter(name = "productId", description = "Product ID", required = true),
                    @Parameter(name = "quantity", description = "New quantity value", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart item quantity updated successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CartResponseDto> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ){
        CartResponseDto response =
                cartSer.updateItemQuantity(userId, productId, quantity);

        return ResponseEntity.ok(response);
    }

//    Checked
    @DeleteMapping("/{userId}/cart/items/{productId}")
    @Operation(
            summary = "Remove item from cart",
            description = "Removes a product from user's cart",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", required = true),
                    @Parameter(name = "productId", description = "Product ID to remove", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item removed from cart successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CartResponseDto> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        CartResponseDto response =
                cartSer.removeItem(userId, productId);

        return ResponseEntity.ok(response);
    }

//    Clear Cart
    @DeleteMapping("/{userId}/cart")
    @Operation(
            summary = "Clear cart",
            description = "Removes all items from user's cart",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cart cleared successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> clearCart(
            @PathVariable Long userId
    ){
        ApiResponseDto response =
                cartSer.clearCart(userId);

        return ResponseEntity.ok(response);
    }
}

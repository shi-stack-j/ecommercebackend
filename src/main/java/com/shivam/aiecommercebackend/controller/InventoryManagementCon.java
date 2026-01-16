package com.shivam.aiecommercebackend.controller;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.service.InventoryManagementSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Tag(
        name = "Inventory",
        description = "APIs for managing product inventory such as stock updates and availability"
)
@RequiredArgsConstructor
public class InventoryManagementCon {

    private final InventoryManagementSer inventoryService;

    private static final String SUCCESS_MESSAGE = "Operation completed successfully";

    /**
     * Reduce stock for a product
     */
//    Checked
    @PatchMapping("/{productId}/reduce-stock")
    @Operation(
            summary = "Reduce product stock",
            description = "Reduces available stock for a product. If quantity is not provided, default reduction logic is applied.",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product",
                            required = true
                    ),
                    @Parameter(
                            name = "quantity",
                            description = "Quantity to reduce from stock (optional)",
                            required = false
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Stock reduced successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> reduceStock(
            @PathVariable Long productId,
            @RequestParam(required = false) Integer quantity
    ) {
        ApiResponseDto response = inventoryService.reduceStock(productId, quantity);
        return ResponseEntity.ok(response);
    }

    /**
     * Increase stock for a product
     */
//    Checked
    @PatchMapping("/{productId}/increase-stock")
    @Operation(
            summary = "Increase product stock",
            description = "Increases available stock for a product. Quantity is optional.",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product",
                            required = true
                    ),
                    @Parameter(
                            name = "quantity",
                            description = "Quantity to increase stock by (optional)",
                            required = false
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Stock increased successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> increaseStock(
            @PathVariable Long productId,
            @RequestParam(required = false) Integer quantity
    ) {
        ApiResponseDto response = inventoryService.increaseStock(productId, quantity);
        return ResponseEntity.ok(response);
    }

    /**
     * Set availability manually
     */
//    Checked
    @PatchMapping("/{productId}/availability")
    @Operation(
            summary = "Set product availability",
            description = "Manually updates product availability status",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product",
                            required = true
                    ),
                    @Parameter(
                            name = "available",
                            description = "Availability status (true = available, false = unavailable)",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product availability updated successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> setAvailability(
            @PathVariable Long productId,
            @RequestParam Boolean available
    ) {
        ApiResponseDto response = inventoryService.setAvailability(productId, available);
        return ResponseEntity.ok(response);
    }
}

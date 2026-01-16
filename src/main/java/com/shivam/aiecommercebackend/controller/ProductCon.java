package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.product.ProductCreateDto;
import com.shivam.aiecommercebackend.dto.product.ProductResponseDto;
import com.shivam.aiecommercebackend.dto.product.ProductUpdateRequestDto;
import com.shivam.aiecommercebackend.service.ProductSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
@Tag(
        name = "Product",
        description = "APIs for managing products, pricing, discounts, images and search"
)
public class ProductCon {
    @Autowired
    private ProductSer productService;

    // ✅ Create Product (ADMIN)
//     checked
    @PostMapping
    @Operation(
            summary = "Create product",
            description = "Creates a new product under a specific category (Admin only)",
            parameters = {
                    @Parameter(
                            name = "categoryName",
                            description = "Category name under which product will be created",
                            required = true,
                            example = "Electronics"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Product creation payload",
                    content = @Content(schema = @Schema(implementation = ProductCreateDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<ApiResponseDto> createProduct(
            @RequestParam String categoryName,
            @Valid @RequestBody ProductCreateDto productCreateDto
    ){
        ApiResponseDto response =
                productService.createProduct(productCreateDto, categoryName);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    checked
    // ✅ Get Product by ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Fetch product details using product ID",
            parameters = {
                    @Parameter(name = "id", description = "Product ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product fetched successfully",
                            content = @Content(schema = @Schema(implementation = ProductResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ProductResponseDto> getProduct(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(productService.getProduct(id));
    }

//    Checked
    // ✅ Get All Products (Pagination)
    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Fetch paginated list of all products",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10")
            }
    )
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

//    Checked
    // ✅ Get Products by Category
    @GetMapping("/category/{categoryName}")
    @Operation(
            summary = "Get products by category",
            description = "Fetch paginated products of a specific category",
            parameters = {
                    @Parameter(name = "categoryName", description = "Category name", required = true),
                    @Parameter(name = "page", example = "0"),
                    @Parameter(name = "size", example = "10")
            }
    )
    public ResponseEntity<Page<ProductResponseDto>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryName, page, size)
        );
    }

//    Checked
    // ✅ Search Products
    @GetMapping("/search")
    @Operation(
            summary = "Search products",
            description = "Search products by keyword (name, description etc.)",
            parameters = {
                    @Parameter(name = "keyword", description = "Search keyword", required = true),
                    @Parameter(name = "page", example = "0"),
                    @Parameter(name = "size", example = "10")
            }
    )
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(
                productService.searchProducts(keyword, page, size)
        );
    }

//    Checked
    // ✅ Update Product (Partial Update)
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update product",
            description = "Partially update product details (Admin only)",
            parameters = {
                    @Parameter(name = "id", description = "Product ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product update payload",
                    content = @Content(schema = @Schema(implementation = ProductUpdateRequestDto.class))
            )
    )
    public ResponseEntity<ApiResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto updateDto
    ){
        return ResponseEntity.ok(
                productService.updateProduct(id, updateDto)
        );
    }


//    Checked
    // ✅ Delete Product
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product",
            description = "Deletes a product permanently (Admin only)",
            parameters = {
                    @Parameter(name = "id", description = "Product ID", required = true)
            }
    )
    public ResponseEntity<ApiResponseDto> deleteProduct(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                productService.deleteProduct(id)
        );
    }

//    Checked
    // ✅ Update Product Price
    @PutMapping("/{id}/price")
    @Operation(
            summary = "Update product price",
            description = "Updates the price of a product",
            parameters = {
                    @Parameter(name = "id", description = "Product ID", required = true),
                    @Parameter(name = "newPrice", description = "New product price", required = true)
            }
    )
    public ResponseEntity<ApiResponseDto> updateProductPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal newPrice
    ){
        return ResponseEntity.ok(
                productService.updateProductPrice(id, newPrice)
        );
    }

//    Checked
    // ✅ Update Product Discount
    @PutMapping("/{id}/discount")
    @Operation(
            summary = "Update product discount",
            description = "Updates discount applied on a product",
            parameters = {
                    @Parameter(name = "id", description = "Product ID", required = true),
                    @Parameter(name = "newDiscount", description = "Discount amount/percentage", required = true)
            }
    )
    public ResponseEntity<ApiResponseDto> updateProductDiscount(
            @PathVariable Long id,
            @RequestParam BigDecimal newDiscount
    ){
        return ResponseEntity.ok(
                productService.updateProductDiscount(id, newDiscount)
        );
    }

    @PutMapping("/{productId}/update-description-ai")
    @Operation(
            summary = "Generate product description using AI",
            description = "Uses AI to automatically generate or improve product description",
            parameters = {
                    @Parameter(name = "productId", description = "Product ID", required = true)
            }
    )
    public ResponseEntity<ApiResponseDto> updateProductDescriptionWithAi(@PathVariable(name = "productId")Long productId){
        ApiResponseDto responseDto=productService.updateProductDescriptionWithAi(productId);
        return ResponseEntity.ok(responseDto);
    }

//    Checked
    @PutMapping(value = "/{productId}/upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload product image",
            description = "Uploads or replaces product image",
            parameters = {
                    @Parameter(name = "productId", description = "Product ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                    )
            )
    )
    public ResponseEntity<ApiResponseDto> uploadImage(@PathVariable(name = "productId")Long productId, @RequestParam(name = "file")MultipartFile file){
        ApiResponseDto responseDto=productService.uploadProductImage(productId,file);
        return ResponseEntity.ok(responseDto);
    }
}

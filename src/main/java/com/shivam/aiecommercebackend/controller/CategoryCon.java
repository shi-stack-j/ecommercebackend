package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryDto;
import com.shivam.aiecommercebackend.dto.category.CategoryResponseDto;
import com.shivam.aiecommercebackend.dto.category.CategoryUpdateRequestDto;
import com.shivam.aiecommercebackend.service.CategorySer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(
        name = "Category",
        description = "APIs for managing product categories and their return policies"
)
public class CategoryCon {
    @Autowired
    private CategorySer categoryService;
    @PostMapping
    @Operation(
            summary = "Create new category",
            description = "Creates a new product category",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Category details",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
//    Checked
    public ResponseEntity<ApiResponseDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto
    ){
        ApiResponseDto response = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Get Category by ID
//    Improvement needed
//    Checked
    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by ID",
            description = "Fetch category details using category ID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            @PathVariable Long id
    ){
        CategoryResponseDto response =
                categoryService.getCategory(String.valueOf(id), "id");
        return ResponseEntity.ok(response);
    }

    // ✅ Get Category by Name
//    Checked
    @GetMapping("/by-name/{name}")
    @Operation(
            summary = "Get category by name",
            description = "Fetch category details using category name",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "Category name",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CategoryResponseDto> getCategoryByName(
            @PathVariable String name
    ){
        CategoryResponseDto response =
                categoryService.getCategory(name, "name");
        return ResponseEntity.ok(response);
    }

    // ✅ Update Category (name / description)
//    Checked
    @PutMapping("/{id}")
    @Operation(
            summary = "Update category",
            description = "Updates category name or description",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated category data",
                    content = @Content(schema = @Schema(implementation = CategoryUpdateRequestDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDto dto
    ){
        CategoryResponseDto response =
                categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(response);
    }

    // ✅ Delete Category
//    Checked
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete category",
            description = "Removes a category by ID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Category ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category removed successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> removeCategory(
            @PathVariable Long id
    ){
        ApiResponseDto response =
                categoryService.removeCategory(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Update Return Policy
//    Checked
    @PutMapping("/{categoryId}/policy/{policyId}")
    @Operation(
            summary = "Update return policy for category",
            description = "Assigns or updates return policy for a category",
            parameters = {
                    @Parameter(name = "categoryId", description = "Category ID", required = true),
                    @Parameter(name = "policyId", description = "Return policy ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return policy updated successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))
                    )
            }
    )
    public ResponseEntity<CategoryResponseDto> updatePolicy(
            @PathVariable Long categoryId,
            @PathVariable Long policyId
    ){
        CategoryResponseDto response =
                categoryService.updatePolicy(categoryId, policyId);
        return ResponseEntity.ok(response);
    }

    // ✅ Get All Categories
//    Checked
    @GetMapping("/getAll")
    @Operation(
            summary = "Get all categories",
            description = "Returns paginated list of all categories",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "5")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categories retrieved successfully"
                    )
            }
    )
    public ResponseEntity<Page<CategoryResponseDto>> getAll(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size
    ){
        Page<CategoryResponseDto> categories=categoryService.getAllCategories(page, size);
        return ResponseEntity.ok(categories);
    }
}

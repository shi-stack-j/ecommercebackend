package com.shivam.aiecommercebackend.controller;


import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.review.ProductReviewRequestDto;
import com.shivam.aiecommercebackend.dto.review.ReviewDto;
import com.shivam.aiecommercebackend.dto.review.ReviewUpdateRequestDto;
import com.shivam.aiecommercebackend.service.ReviewSer;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@Tag(
        name = "Review",
        description = "APIs for managing product reviews, ratings, and user feedback"
)
public class ReviewCon {
    @Autowired
    private ReviewSer reviewService;

    // ✅ Add Review
//    Checked
    @PostMapping("/products/{productId}/reviews")
    @Operation(
            summary = "Add product review",
            description = "Allows a user to submit a review and rating for a specific product",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product to review",
                            required = true,
                            example = "101"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Review payload including rating and comment",
                    content = @Content(
                            schema = @Schema(implementation = ProductReviewRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Review added successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ReviewDto.class)
                            )
                    )
            }
    )

    public ResponseEntity<ReviewDto> addReview(
            @PathVariable Long productId,

            @Valid @RequestBody ProductReviewRequestDto reviewDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.addReview(reviewDto, productId));
    }

    // ✅ Update Review
//    Checked
    @PatchMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Update review",
            description = "Allows a user to update an existing review",
            parameters = {
                    @Parameter(
                            name = "reviewId",
                            description = "ID of the review to update",
                            required = true,
                            example = "55"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated review details",
                    content = @Content(
                            schema = @Schema(implementation = ReviewUpdateRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ReviewDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long reviewId,
//            @RequestParam Long userId,
            @RequestBody ReviewUpdateRequestDto updateDto
    ){
        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, updateDto)
        );
    }

    // ✅ Delete Review
//    Checked
    @DeleteMapping("/reviews/{reviewId}")
    @Operation(
            summary = "Delete review",
            description = "Deletes a review created by the user",
            parameters = {
                    @Parameter(
                            name = "reviewId",
                            description = "ID of the review to delete",
                            required = true,
                            example = "55"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review deleted successfully")
            }
    )
    public ResponseEntity<ApiResponseDto> deleteReview(
            @PathVariable Long reviewId
//            @RequestParam Long userId
    ){
        return ResponseEntity.ok(
                reviewService.deleteReview(reviewId)
        );
    }

    // ✅ Get Product Reviews
//
    @GetMapping("/products/{productId}/reviews")
    @Operation(
            summary = "Get product reviews",
            description = "Fetch paginated list of reviews for a specific product",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product",
                            required = true,
                            example = "101"
                    ),
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product reviews fetched successfully"
                    )
            }
    )
    public ResponseEntity<Page<ReviewDto>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(
                reviewService.getProductReviews(productId, page, size)
        );
    }

    // ✅ Get User Reviews
//    Checked
    @GetMapping("/users/{userId}/reviews")
    @Operation(
            summary = "Get user reviews",
            description = "Fetch paginated list of reviews submitted by a specific user",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user",
                            required = true,
                            example = "12"
                    ),
                    @Parameter(name = "page", example = "0"),
                    @Parameter(name = "size", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User reviews fetched successfully"
                    )
            }
    )
    public ResponseEntity<Page<ReviewDto>> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(
                reviewService.getUserReview(userId, page, size)
        );
    }

    // ✅ Average Rating
    @GetMapping("/products/{productId}/rating")
    @Operation(
            summary = "Get average product rating",
            description = "Fetch average rating of a product based on user reviews",
            parameters = {
                    @Parameter(
                            name = "productId",
                            description = "ID of the product",
                            required = true,
                            example = "101"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Average rating fetched successfully",
                            content = @Content(
                                    schema = @Schema(implementation = Double.class)
                            )
                    )
            }
    )
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(
                reviewService.getAverageRating(productId)
        );
    }
}

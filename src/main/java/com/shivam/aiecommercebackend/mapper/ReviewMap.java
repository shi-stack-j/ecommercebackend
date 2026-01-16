package com.shivam.aiecommercebackend.mapper;

import com.shivam.aiecommercebackend.dto.review.ReviewDto;
import com.shivam.aiecommercebackend.entity.ReviewEn;

public final class ReviewMap {
    public static ReviewDto toReviewDto(ReviewEn reviewEn){
        ReviewDto reviewDto=new ReviewDto();
        reviewDto.setId(reviewEn.getId());
        reviewDto.setDescription(reviewEn.getDescription());
        reviewDto.setRating(reviewEn.getRating());
        reviewDto.setUsername(reviewEn.getUserEn().getUsername());
        reviewDto.setProductId(reviewEn.getProductEn().getId());
        reviewDto.setUserId(reviewEn.getUserEn().getId());
        reviewDto.setCreatedAt(reviewEn.getCreatedAt());
        reviewDto.setUpdatedAt(reviewEn.getUpdatedAt());
        return reviewDto;
    }

}

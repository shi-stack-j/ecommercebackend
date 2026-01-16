package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.review.ProductReviewRequestDto;
import com.shivam.aiecommercebackend.dto.review.ReviewDto;
import com.shivam.aiecommercebackend.dto.review.ReviewUpdateRequestDto;
import com.shivam.aiecommercebackend.entity.ProductEn;
import com.shivam.aiecommercebackend.entity.ReviewEn;
import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.exception.AccessDeniedException;
import com.shivam.aiecommercebackend.exception.AlreadyReviewedException;
import com.shivam.aiecommercebackend.exception.InvalidInputException;
import com.shivam.aiecommercebackend.exception.ResourceNotFoundException;
import com.shivam.aiecommercebackend.mapper.ReviewMap;
import com.shivam.aiecommercebackend.repository.OrderEnRepo;
import com.shivam.aiecommercebackend.repository.ProductEnRepo;
import com.shivam.aiecommercebackend.repository.ReviewEnRepo;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
public class ReviewSer {
    @Autowired
    private ReviewEnRepo reviewRepo;
    @Autowired
    private OrderEnRepo orderRepo;
    @Autowired
    private UserEnRepo userRepo;
    @Autowired
    private ProductEnRepo productRepo;
//    Operation	                Service Type
//    Add review	            Separate service

    @PreAuthorize("@auth.canMakeReview(#productId)")
    @Transactional
    public ReviewDto addReview(ProductReviewRequestDto reviewDto,Long productId){
        if(reviewDto==null)throw new InvalidInputException("Review Dto cannot be null");
//        if(userId==null || userId<=0)throw new InvalidInputException("UserId cannot be null or equal to less then zero");
        if(productId==null || productId<=0)throw new InvalidInputException("Product cannot be null or equal to less then zero");
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsPrincipal userDetailsPrincipal=(UserDetailsPrincipal) authentication.getPrincipal();
        Long userId=userDetailsPrincipal.getId();
        boolean isReviewed=reviewRepo.existsByUserEn_IdAndProductEn_Id(userId,productId);
        if(isReviewed)throw new AlreadyReviewedException("User has already reviewed the product");
        boolean isValid=orderRepo.existsByUserEn_IdAndOrderStatusAndItems_ProductEn_Id(userId, OrderStatus.DELIVERED,productId);
        if(!isValid) throw new AccessDeniedException("Could Not Review the product");
        UserEn userEn=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Not found with the given userId :- "+userId));
        ProductEn productEn=productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product Not found with the given productId :- "+productId));
        ReviewEn reviewEn=new ReviewEn();
        reviewEn.setDescription(reviewDto.getDescription());
        reviewEn.setRating(reviewDto.getRating());
        reviewEn.setUserEn(userEn);
        reviewEn.setProductEn(productEn);
        ReviewEn review=reviewRepo.save(reviewEn);
        return ReviewMap.toReviewDto(review);
    }

    @PreAuthorize("@auth.canUpdateReview(#reviewId)")
//    Update review	            Separate
    @Transactional
    public ReviewDto updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdate){
        if(reviewId==null || reviewId<=0)throw new InvalidInputException("Review is not valid...");
        if(reviewUpdate==null)throw new InvalidInputException("Review Dto cannot be null");
        boolean isRatingInvalid= reviewUpdate.getRating()==null ;
        boolean isDescriptionInvalid=reviewUpdate.getDescription()==null || reviewUpdate.getDescription().isBlank();
        if(isRatingInvalid && isDescriptionInvalid)throw new InvalidInputException("Both description and rating cannot be null");
        ReviewEn reviewEn=reviewRepo.findById(reviewId)
                .orElseThrow(()->new ResourceNotFoundException("Review not found with the given id :- "+reviewId));

        if(!isRatingInvalid ){
            if(reviewUpdate.getRating() < 1 || reviewUpdate.getRating() > 5){
                throw new InvalidInputException("Rating must be between 1 and 5");
            }
            reviewEn.setRating(reviewUpdate.getRating());
        }

        if(!isDescriptionInvalid)reviewEn.setDescription(reviewUpdate.getDescription().trim());
        reviewRepo.save(reviewEn);
        return ReviewMap.toReviewDto(reviewEn);
    }
//    Delete review	            Separate
    @PreAuthorize("@auth.canUpdateReview(#reviewId)")
    @Transactional
    public ApiResponseDto deleteReview(Long reviewId){
        if(reviewId==null || reviewId<=0)throw new InvalidInputException("Review Id is not valid");
        ReviewEn reviewEn=reviewRepo.findById(reviewId)
                .orElseThrow(()->new ResourceNotFoundException("Review Not found with the given id :- "+reviewId));
        reviewRepo.delete(reviewEn);
        return ApiResponseDto.builder()
                .time(LocalDateTime.now())
                .status(ApiResponseStatusEnum.SUCCESS)
                .message("Review Deleted Successfully...")
                .build();
    }
//    Get reviews (product) 	Read-only
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<ReviewDto> getProductReviews(Long productId,Integer page,Integer size){
        if(productId==null || productId<=0)throw new InvalidInputException("Product Id is not valid");
        if(page==null || page<0)throw new InvalidInputException("Page number is not valid");
        if(size==null || size<=0 || size>=100)throw new InvalidInputException("Size is not valid");
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        boolean productEn=productRepo.existsById(productId);
        if(!productEn)throw new ResourceNotFoundException("Product not found with the given id :- "+productId);
        Page<ReviewEn> reviews=reviewRepo.findByProductEn_Id(productId,pageable);
        return reviews.map(ReviewMap::toReviewDto);
    }
//    Get reviews (user)	    Read-only
    @PreAuthorize("hasRole('ADMIN') or #userId==principal.id")
    @Transactional(readOnly = true)
    public Page<ReviewDto> getUserReview(Long userId,Integer page,Integer size){
        if(userId==null || userId<=0)throw new InvalidInputException("User Id is not valid");
        if(page==null || page<0)throw new InvalidInputException("Page number is not valid");
        if(size==null || size<=0 || size>=100)throw new InvalidInputException("Size is not valid");
        boolean userEn=userRepo.existsById(userId);
        if (!userEn) throw new ResourceNotFoundException("User not found with the given id :- "+userId);
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<ReviewEn> reviews=reviewRepo.findByUserEn_Id(userId,pageable);
        return reviews.map(ReviewMap::toReviewDto);
    }

    @PreAuthorize("isAuthenticated()")
//    Average rating	        Read-only
    @Transactional(readOnly = true)
    public Double getAverageRating(Long productId){
        if(productId==null || productId<=0)throw new InvalidInputException("Product Id is not valid");
        boolean productEn=productRepo.existsById(productId);
        if(!productEn) throw  new ResourceNotFoundException("Product not found with the given id :- "+productId);
        Double avgRating=reviewRepo.findAverageByProductId(productId);
        if (avgRating==null)return 0.0;
        return Math.round(avgRating*10)/10.0;
    }
}

package com.shivam.aiecommercebackend.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long id;

    private int rating;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long productId;
    private Long userId;
    private String username;
}

package com.shivam.aiecommercebackend.dto.category;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long id;
    private Long policyId;
}

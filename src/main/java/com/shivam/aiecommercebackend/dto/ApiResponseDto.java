package com.shivam.aiecommercebackend.dto;

import com.shivam.aiecommercebackend.enums.ApiResponseStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto {
    private String message;
    private ApiResponseStatusEnum status;
    private LocalDateTime time;
}

package com.shivam.aiecommercebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String errorCode;
    private LocalDateTime timeStamp;
}

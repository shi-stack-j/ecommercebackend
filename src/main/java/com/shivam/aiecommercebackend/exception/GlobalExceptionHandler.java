package com.shivam.aiecommercebackend.exception;

import com.shivam.aiecommercebackend.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto>  userNotFoundException(ResourceNotFoundException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
//                ex.getMessage(),
//                "NOT_FOUND",
//                LocalDateTime.now()
//        );
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(errorResponseDto);
//    }
//    @ExceptionHandler(DuplicationEntryException.class)
//    public ResponseEntity<ErrorResponseDto> duplicateEntryException(DuplicationEntryException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
//                ex.getMessage(),
//                "BAD_REQUEST",
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
//    }
//    @ExceptionHandler(InvalidInputException.class)
//    public ResponseEntity<ErrorResponseDto> invalidInputException(InvalidInputException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto();
//        errorResponseDto.setErrorCode("BAD_REQUEST");
//        errorResponseDto.setTimeStamp(LocalDateTime.now());
//        errorResponseDto.setMessage(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
//    }
//    @ExceptionHandler(AlreadyReviewedException.class)
//    public ResponseEntity<ErrorResponseDto> alreadyReviewed(AlreadyReviewedException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto();
//        errorResponseDto.setErrorCode("BAD_REQUEST");
//        errorResponseDto.setTimeStamp(LocalDateTime.now());
//        errorResponseDto.setMessage(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
//    }
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponseDto> accessDenied(AccessDeniedException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto errorResponseDto=new ErrorResponseDto();
//        errorResponseDto.setMessage(ex.getMessage());
//        errorResponseDto.setTimeStamp(LocalDateTime.now());
//        errorResponseDto.setErrorCode("BAD_REQUEST");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
//    }
//    @ExceptionHandler(ReturnWindowExpirationException.class)
//    public ResponseEntity<ErrorResponseDto> returnWindowExpire(ReturnWindowExpirationException ex){
//        log.error(ex.getMessage());
//        ErrorResponseDto responseDto=new ErrorResponseDto();
//        responseDto.setErrorCode("BAD_REQUEST");
//        responseDto.setTimeStamp(LocalDateTime.now());
//        responseDto.setMessage(ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({
            InvalidInputException.class,
            DuplicationEntryException.class,
            ReturnWindowExpirationException.class,
            AlreadyReviewedException.class,

    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex){
        return errorBuild(ex,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex){
        return errorBuild(ex,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex){
        return errorBuild(ex,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> genericException(Exception e){
        return errorBuild(e,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDto> errorBuild(Exception ex, HttpStatus status){
        log.error(ex.getMessage());
        ErrorResponseDto errorResponseDto=new ErrorResponseDto(
                ex.getMessage(),
                status.name(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponseDto);
    }
}

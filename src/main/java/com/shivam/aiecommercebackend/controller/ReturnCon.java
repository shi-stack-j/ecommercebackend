package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.returnDtos.ReturnRequestDto;
import com.shivam.aiecommercebackend.dto.returnDtos.ReturnResponseDto;
import com.shivam.aiecommercebackend.service.ReturnSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/return")
@Tag(
        name = "Return",
        description = "APIs related to return requests, tracking, and return processing for user orders"
)
public class ReturnCon {
    @Autowired
    private ReturnSer returnService;
    @PostMapping
//    Checked
    @Operation(
            summary = "Raise return request",
            description = "Allows a user to raise a return request for an order item",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user raising the return request",
                            required = true,
                            example = "12"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Return request payload containing order and reason details",
                    content = @Content(
                            schema = @Schema(implementation = ReturnRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Return request created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ReturnResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReturnResponseDto> raiseReturnRequest(
            @RequestParam Long userId,
            @Valid @RequestBody ReturnRequestDto requestDto
    ) {
        ReturnResponseDto response = returnService.raiseReturnRequest(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

//    Checked
    @GetMapping("/{returnId}")
    @Operation(
            summary = "Get return request details",
            description = "Fetch return request details using return ID for a specific user",
            parameters = {
                    @Parameter(
                            name = "returnId",
                            description = "Unique ID of the return request",
                            required = true,
                            example = "501"
                    ),
                    @Parameter(
                            name = "userId",
                            description = "ID of the user who raised the return request",
                            required = true,
                            example = "12"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return request retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ReturnResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReturnResponseDto> getReturnId(
            @PathVariable Long returnId,
            @RequestParam Long userId
    ){
        ReturnResponseDto response=returnService.getReturnRequest(returnId,userId);
        return ResponseEntity.ok(response);
    }
}

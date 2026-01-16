package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.ApiResponseDto;
import com.shivam.aiecommercebackend.dto.user.UserDto;
import com.shivam.aiecommercebackend.dto.returnDtos.ReturnResponseDto;
import com.shivam.aiecommercebackend.enums.RoleEnum;
import com.shivam.aiecommercebackend.enums.UserSearchEnum;
import com.shivam.aiecommercebackend.service.AdminSer;
import com.shivam.aiecommercebackend.service.UserSer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(
        name = "Admin",
        description = "APIs for admin operations such as managing users, roles and return requests"
)
public class AdminCon {
    @Autowired
    private AdminSer adminSer;
    @Autowired
    private UserSer userService;

//    Checked
    @PatchMapping("/{username}/status")
    @Operation(
            summary = "Enable or disable user account",
            description = "Allows admin to enable or disable a user's account using username and status flag",
            parameters = {
                    @Parameter(name = "username", description = "Username of the user", required = true),
                    @Parameter(name = "enabled", description = "Account status (true = enable, false = disable)", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User account status updated successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> changeUserAccountStatus(
            @PathVariable String username,
            @RequestParam Boolean enabled
    ) {
        ApiResponseDto response =
                userService.changeUserAccountStatus(username, enabled);

        return ResponseEntity.ok(response);
    }

//    Checked
    @PutMapping("/user/{username}/role")
    @Operation(
            summary = "Update user role",
            description = "Allows admin to update role of a user",
            parameters = {
                    @Parameter(name = "username", description = "Username of the user", required = true),
                    @Parameter(
                            name = "roleEnum",
                            description = "New role to assign",
                            required = true,
                            schema = @Schema(implementation = RoleEnum.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User role updated successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ApiResponseDto> updateUserRole(
            @PathVariable String username,
            @RequestParam(value = "roleEnum") RoleEnum roleEnum)
    {
        ApiResponseDto response=adminSer.updateUserRole(username,roleEnum);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

//    Checked
    @GetMapping("/user/getAll")
    @Operation(
            summary = "Get all users",
            description = "Returns paginated list of all registered users",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Number of records per page", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users retrieved successfully"
                    )
            }
    )
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size

    ){
        Page<UserDto> users=adminSer.getAllUsers(page,size);
        return ResponseEntity.ok(users);
    }

//    Checked
    @GetMapping("/user/getUser")
    @Operation(
            summary = "Get user by value and type",
            description = "Fetch user using username, email or other supported search type",
            parameters = {
                    @Parameter(name = "value", description = "Search value", required = true),
                    @Parameter(
                            name = "type",
                            description = "Search type",
                            required = true,
                            schema = @Schema(implementation = UserSearchEnum.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            }
    )
    public ResponseEntity<UserDto> getUser(
            @RequestParam(name = "value") String value,
            @RequestParam(name = "type") UserSearchEnum type
    ){
        UserDto response=adminSer.getUser(value,type.name());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/return/approveReturnRequest")
    @Operation(
            summary = "Approve return request",
            description = "Approves a pending return request",
            parameters = {
                    @Parameter(name = "returnId", description = "ID of the return request", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return request approved successfully",
                            content = @Content(schema = @Schema(implementation = ReturnResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ReturnResponseDto> approveReturnRequest(
            @RequestParam(name = "returnId") Long returnId
    ){
        ReturnResponseDto response=adminSer.approveReturn(returnId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/return/rejectReturnRequest")
    @Operation(
            summary = "Reject return request",
            description = "Reject a pending return request",
            parameters = {
                    @Parameter(name = "returnId", description = "ID of the return request", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return request Rejected successfully",
                            content = @Content(schema = @Schema(implementation = ReturnResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ReturnResponseDto> rejectReturnRequest(
            @RequestParam(name="returnId") Long returnId
    ){
        ReturnResponseDto response=adminSer.rejectReturn(returnId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/return/completeReturnRequest")
    @Operation(
            summary = "Complete return request",
            description = "Completes the approves return  request",
            parameters = {
                    @Parameter(name = "returnId", description = "ID of the return request", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return request completed successfully",
                            content = @Content(schema = @Schema(implementation = ReturnResponseDto.class))
                    )
            }
    )
    public ResponseEntity<ReturnResponseDto> completeReturnRequest(
            @RequestParam(name="returnId") Long returnId
    ){
        ReturnResponseDto response=adminSer.completeReturn(returnId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/return/getPendingReturnRequests")
    @Operation(
            summary = "Get pending return requests",
            description = "Returns paginated list of all pending return requests",
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pending return requests retrieved successfully"
                    )
            }
    )
    public ResponseEntity<Page<ReturnResponseDto>> getPendingReturnRequest(
            @RequestParam(name = "page",defaultValue = "0")Integer page,
            @RequestParam(name = "size",defaultValue = "10")Integer size
    ){
        Page<ReturnResponseDto> response=adminSer.getPendingReturns(page,size);
        return ResponseEntity.ok(response);
    }

}


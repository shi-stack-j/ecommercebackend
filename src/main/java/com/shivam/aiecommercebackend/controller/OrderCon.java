package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.order.CreateOrderDto;
import com.shivam.aiecommercebackend.dto.order.OrderResponseDto;
import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import com.shivam.aiecommercebackend.service.OrderSer;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/order")
@Tag(
        name = "Order",
        description = "APIs for placing, viewing, cancelling and managing orders"
)
public class OrderCon {
    @Autowired
    private OrderSer orderService;
    // ✅ Place Order
//    Checked
    @PostMapping("/users/{userId}/orders")
    @Operation(
            summary = "Place an order",
            description = "Places a new order for a user using cart and delivery details",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user placing the order",
                            required = true
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Order creation payload",
                    content = @Content(schema = @Schema(implementation = CreateOrderDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order placed successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
                    )
            }
    )
    public ResponseEntity<OrderResponseDto> placeOrder(
            @PathVariable Long userId,
            @Valid @RequestBody CreateOrderDto orderDto
    ){
        OrderResponseDto response =
                orderService.placeOrder(userId, orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Get Order by ID
//    Checked
    @GetMapping("/orders/{orderID}")
    @Operation(
            summary = "Get order by ID",
            description = "Fetch order details using order ID",
            parameters = {
                    @Parameter(
                            name = "orderID",
                            description = "Order ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
                    )
            }
    )
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable Long orderID
    ){
        OrderResponseDto response =
                orderService.getOrderByID(orderID);

        return ResponseEntity.ok(response);
    }

    // ✅ Get Orders of a User
//    Checked
    @GetMapping("/users/{userId}/orders")
    @Operation(
            summary = "Get user's orders",
            description = "Returns paginated list of orders placed by a user",
            parameters = {
                    @Parameter(name = "userId", description = "User ID", required = true),
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders retrieved successfully"
                    )
            }
    )
    public ResponseEntity<Page<OrderResponseDto>> getOrdersOfUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Page<OrderResponseDto> response =
                orderService.getOrderOfUser(userId, page, size);

        return ResponseEntity.ok(response);
    }

    // ✅ Cancel Order
//    Checked
    @PutMapping("/orders/{orderId}/cancel")
    @Operation(
            summary = "Cancel order",
            description = "Cancels an order if it is eligible for cancellation",
            parameters = {
                    @Parameter(
                            name = "orderId",
                            description = "Order ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order cancelled successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
                    )
            }
    )
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId
    ){
        OrderResponseDto response =
                orderService.cancelOrder(orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
//    Checked
    @Operation(
            summary = "Get all orders",
            description = "Fetch paginated list of all orders with optional filters",
            parameters = {
                    @Parameter(name = "mode", description = "Payment mode"),
                    @Parameter(name = "fromDate", description = "Start date (yyyy-MM-dd)"),
                    @Parameter(name = "toDate", description = "End date (yyyy-MM-dd)"),
                    @Parameter(name = "orderStatus", description = "Order status"),
                    @Parameter(name = "minimumAmount", description = "Minimum order amount"),
                    @Parameter(name = "maxAmount", description = "Maximum order amount"),
                    @Parameter(name = "page", description = "Page number", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Orders retrieved successfully"
                    )
            }
    )
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
            @RequestParam(required = false) PaymentMode mode,
            @RequestParam(required = false) LocalDate fromDate, //yyyy-MM-dd
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) BigDecimal minimumAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ){
        Page<OrderResponseDto> response =
                orderService.getAllOrders(
                        mode, fromDate, toDate,
                        orderStatus, minimumAmount, maxAmount,
                        page, size
                );

        return ResponseEntity.ok(response);
    }

    // ✅ Update Order Status
//    Checked
    @PutMapping("/{orderId}/status")
    @Operation(
            summary = "Update order status",
            description = "Updates the status of an order (Admin operation)",
            parameters = {
                    @Parameter(name = "orderId", description = "Order ID", required = true),
                    @Parameter(
                            name = "status",
                            description = "New order status",
                            required = true,
                            schema = @Schema(implementation = OrderStatus.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order status updated successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))
                    )
            }
    )
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ){
        OrderResponseDto response =
                orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(response);
    }
}

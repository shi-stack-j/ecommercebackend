package com.shivam.aiecommercebackend.dto.returnDtos;

import com.shivam.aiecommercebackend.enums.ReturnStatusEnum;
import com.shivam.aiecommercebackend.enums.ReturnTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnResponseDto {
    private Long returnId;
    private Long orderItemId;
//    This will we the custom message from our side on the bases of the Status
    private String message;
    private LocalDateTime requestedAt;
    private ReturnStatusEnum returnStatusEnum;
    private ReturnTypeEnum returnTypeEnum;
    private BigDecimal refundableAmount;
    private String reason;
}

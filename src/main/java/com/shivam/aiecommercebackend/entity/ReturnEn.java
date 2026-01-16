package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.ReturnStatusEnum;
import com.shivam.aiecommercebackend.enums.ReturnTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReturnEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 10, max = 200,message = "Reason must be b/w 10 - 200 size")
    private String reason;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Return type is must ")
    private ReturnTypeEnum returnTypeEnm;

    @Enumerated(EnumType.STRING)
    private ReturnStatusEnum returnStatusEnum=ReturnStatusEnum.REQUESTED;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestedAt;

    @UpdateTimestamp
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEn userEn;

    @ManyToOne
    @JoinColumn(name ="order_item_id",nullable = false)
    private OrderItem orderItem;

    @NotNull(message = "RefundableAmount cannot be null")
    @PositiveOrZero(message = "Refundable amount cannot be negative")
    private BigDecimal refundableAmount;

    @OneToOne(mappedBy = "returnEn", cascade = CascadeType.ALL )
    private RefundPaymentEn refundPayment;

    @OneToOne
    private OrderItem replacementOrderItem;
}

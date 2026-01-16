package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.RefundModeEnum;
import com.shivam.aiecommercebackend.enums.RefundStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundPaymentEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 One return → one refund record
    @OneToOne(optional = false)
    @JoinColumn(name = "return_id", nullable = false, unique = true)
    private ReturnEn returnEn;

    // 💰 Refunded Amount
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    // 💳 Mode of refund
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundModeEnum refundMode;

    // 📦 Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatusEnum refundStatus;

    // 🔑 Gateway / Transaction reference (optional)
    @Column(length = 100)
    private String transactionRef;

    // 📝 Failure reason (if any)
    @Column(length = 255)
    private String failureReason;

    // ⏱️ Timestamps
    @CreationTimestamp
    private LocalDateTime initiatedAt;
    @UpdateTimestamp
    private LocalDateTime completedAt;
}

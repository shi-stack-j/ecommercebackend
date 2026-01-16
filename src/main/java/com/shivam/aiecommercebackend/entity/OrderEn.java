package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.OrderStatus;
import com.shivam.aiecommercebackend.enums.PaymentMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,updatable = false)
    @NotNull(message = "Price is required")
    @PositiveOrZero
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String address;

    @Column(unique = true)
    private String trackingNumber;

    private int totalItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus=OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;
//    Customer id
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEn userEn;
//    Order Items
    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "orderEn",fetch = FetchType.LAZY)
    private List<OrderItem> items=new ArrayList<>();
}

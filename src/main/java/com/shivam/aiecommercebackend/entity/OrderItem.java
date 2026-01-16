package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal price;
    @Column(nullable = false)
    private int quantity=1;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnPolicyCapabilities returnCapabilities;

    @PositiveOrZero
    @Column(nullable = false)
    @Min(value = 0,message = "minimum window size is  0")
    @Max(value = 16,message = "maximum window size is 16")
    private Integer windowSize;

//    Product link
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEn productEn;
//    Added to order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEn orderEn;


    private LocalDateTime returnRequestedAt;
}

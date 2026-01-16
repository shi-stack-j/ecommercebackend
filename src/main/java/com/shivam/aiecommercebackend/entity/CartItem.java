package com.shivam.aiecommercebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Enter price")
    @PositiveOrZero
    private BigDecimal price;
    @PositiveOrZero
    private int quantity=1;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
//    Added the product id
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private ProductEn productEn;
//    And the card id
    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id",nullable = false)
    private CartEn cart;
}

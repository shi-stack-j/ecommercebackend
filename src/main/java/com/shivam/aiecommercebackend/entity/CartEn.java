package com.shivam.aiecommercebackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CartEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount=BigDecimal.ZERO;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
//    public void calculateTotal() {
//        this.totalAmount = items.stream()
//                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
    public void calculateTotal() {
        // 1. Get the values() collection from the Map
        // 2. Stream over the CartItemEn objects
        this.totalAmount = this.items.values().stream()

                // Map each CartItemEn to its subtotal (price * quantity)
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))

                // Sum all the subtotals to get the grand total
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
//    customer id
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private UserEn userEn;
//    Cart items
    @OneToMany(mappedBy = "cart",orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapKeyJoinColumn(name = "product_id")
    private Map<Long,CartItem> items=new HashMap<>();
}

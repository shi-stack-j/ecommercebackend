package com.shivam.aiecommercebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@ToString
@Getter
@Builder
@Setter
public class ProductEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal price=BigDecimal.valueOf(0);

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer quantity=0;

    @Column(nullable = false)
    private Boolean availability = true;
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

    @Column(unique = true,updatable =false,nullable = false)
    private String sku;

    @Size(max = 255)
    private String imageUrl;

    @PositiveOrZero
    @NotNull
    private BigDecimal discount=BigDecimal.valueOf(0);

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "productEn",fetch = FetchType.LAZY)
    private List<OrderItem> orderItem=new ArrayList<>();

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "productEn",fetch = FetchType.LAZY)
    private List<CartItem> cartItems=new ArrayList<>();

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,mappedBy = "productEn",fetch = FetchType.LAZY)
    private List<ReviewEn> reviews=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private CategoryEn categoryEn;

    private String publicId;
}


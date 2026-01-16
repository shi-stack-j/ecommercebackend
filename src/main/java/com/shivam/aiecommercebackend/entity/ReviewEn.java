package com.shivam.aiecommercebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "product_id"}
        )
)

public class ReviewEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull(message = "Rating required")
    @Min(1)
    @Max(5)
    @Positive
    private int rating;
    @Size(min = 5, max = 50)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //    Product id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEn productEn;
    //    User id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEn userEn;
}

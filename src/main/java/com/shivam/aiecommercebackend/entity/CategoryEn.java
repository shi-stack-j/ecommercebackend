package com.shivam.aiecommercebackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CategoryEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Category name is must to be mentioned")
    @Column(nullable = false,unique =true)
    private String name;
    @Size(max = 200)
    private String description;
    private String imageUrl;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    Products details
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "categoryEn",fetch = FetchType.LAZY)
    private List<ProductEn> productEns=new ArrayList<>();

//    Return policy
    @ManyToOne
    @JoinColumn(name = "policy_id",nullable = false)
    private ReturnPolicyEn policyEn;
}

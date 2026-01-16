package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class UserEn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotNull(message = "Username is required")
    @Size(min = 5)
    private String username;
    @NotNull(message = "Name is required")
    private String name;
    @Email(message = "Enter valid email")
    @Column(nullable = false,unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private boolean isEnabled=true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role=RoleEnum.ROLE_USER;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updatedAt;
//    USER cart
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "userEn")
    private CartEn cartEn;
//    Orders
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "userEn",fetch = FetchType.LAZY)
    private List<OrderEn> orders=new ArrayList<>();
//    Reviews
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "userEn",fetch = FetchType.LAZY)
    private  List<ReviewEn> reviewEns=new ArrayList<>();
}


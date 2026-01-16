package com.shivam.aiecommercebackend.entity;

import com.shivam.aiecommercebackend.enums.ReturnPolicyCapabilities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "return_policies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_return_policy_rule",
                        columnNames = {"capabilities", "window_size"}
                )
        }
)
public class ReturnPolicyEn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Policy name Must not be NULL")
    @NotBlank(message = "Policy Name must not be BLANK")
    @Size(min = 2,message = "Size must be greater then 2")
    @Column(unique = true)
    private String policyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnPolicyCapabilities capabilities;

    @PositiveOrZero
    @Min(value = 0, message = "Window size cannot be less than zero")
    @Max(value = 16, message = "Window size cannot be greater than 16")
    @Column(name = "window_size", nullable = false)
    private Integer windowSize;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active = true;
}
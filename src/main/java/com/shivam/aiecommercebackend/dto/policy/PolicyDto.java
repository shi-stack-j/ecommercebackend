package com.shivam.aiecommercebackend.dto.policy;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyDto {
    private Long id;
    private String capabilities;
    private Integer windowSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
    private String policyName;
}

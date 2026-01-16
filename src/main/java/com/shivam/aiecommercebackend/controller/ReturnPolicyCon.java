package com.shivam.aiecommercebackend.controller;

import com.shivam.aiecommercebackend.dto.policy.PolicyCreateDto;
import com.shivam.aiecommercebackend.dto.policy.PolicyDto;
import com.shivam.aiecommercebackend.dto.policy.PolicyUpdateRequestDto;
import com.shivam.aiecommercebackend.service.ReturnPolicySer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/return/policy/return/policy")
@Tag(
        name = "Return Policy",
        description = "APIs for managing return policies including creation, activation, deactivation, and updates"
)
public class ReturnPolicyCon {

    @Autowired
    private ReturnPolicySer returnPolicyService;

    /**
     * Create a new return policy
     *
     */

//    Checked
    @PostMapping
    @Operation(
            summary = "Create return policy",
            description = "Creates a new return policy that can later be assigned to product categories",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Return policy creation payload",
                    content = @Content(
                            schema = @Schema(implementation = PolicyCreateDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Return policy created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PolicyDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PolicyDto> createPolicy(@Valid @RequestBody PolicyCreateDto createDto) {
        PolicyDto policy = returnPolicyService.createPolicy(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(policy);
    }

    /**
     * Get a policy by its ID
     */

//    Checked
    @GetMapping("/{policyId}")
    @Operation(
            summary = "Get return policy by ID",
            description = "Fetch details of a return policy using its unique identifier",
            parameters = {
                    @Parameter(
                            name = "policyId",
                            description = "Unique ID of the return policy",
                            required = true,
                            example = "5"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return policy retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PolicyDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PolicyDto> getPolicy(@PathVariable Long policyId) {
        PolicyDto policy = returnPolicyService.getPolicy(policyId);
        return ResponseEntity.ok(policy);
    }

    /**
     * Deactivate a policy
     */

//   Checked
    @PatchMapping("/{policyId}/deactivate")
    @Operation(
            summary = "Deactivate return policy",
            description = "Deactivates an active return policy so it can no longer be used",
            parameters = {
                    @Parameter(
                            name = "policyId",
                            description = "ID of the return policy to deactivate",
                            required = true,
                            example = "5"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return policy deactivated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PolicyDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PolicyDto> deactivatePolicy(@PathVariable Long policyId) {
        PolicyDto policy = returnPolicyService.deactivatePolicy(policyId);
        return ResponseEntity.ok(policy);
    }

    /**
     * Activate a policy
     */

//    Checked
    @PatchMapping("/{policyId}/activate")
    @Operation(
            summary = "Activate return policy",
            description = "Activates an inactive return policy making it available for use",
            parameters = {
                    @Parameter(
                            name = "policyId",
                            description = "ID of the return policy to activate",
                            required = true,
                            example = "5"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return policy activated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PolicyDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PolicyDto> activatePolicy(@PathVariable Long policyId) {
        PolicyDto policy = returnPolicyService.activatePolicy(policyId);
        return ResponseEntity.ok(policy);
    }

    /**
     * Update a policy (partial update)
     */
//   Checked
    @PatchMapping("/{policyId}")
    @Operation(
            summary = "Update return policy",
            description = "Partially updates an existing return policy such as duration, conditions, or description",
            parameters = {
                    @Parameter(
                            name = "policyId",
                            description = "ID of the return policy to update",
                            required = true,
                            example = "5"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Return policy update payload",
                    content = @Content(
                            schema = @Schema(implementation = PolicyUpdateRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Return policy updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PolicyDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<PolicyDto> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody PolicyUpdateRequestDto updateRequestDto) {
        PolicyDto policy = returnPolicyService.updatePolicy(policyId, updateRequestDto);
        return ResponseEntity.ok(policy);
    }
}

package com.scholarpath.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank @Size(max = 100) String course,
        @NotNull @Min(1) @Max(6) Integer academicYear,
        @NotNull @DecimalMin("0.0") @DecimalMax("100.0") Double academicPercentage,
        @NotNull @Min(0) Long annualFamilyIncome,
        @NotBlank @Size(max = 100) String state,
        @NotBlank @Size(max = 50) String category) {
}

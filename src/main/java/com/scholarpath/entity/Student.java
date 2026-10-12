package com.scholarpath.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(length = 100)
    private String passwordHash;

    @NotBlank(message = "Course is required")
    @Column(nullable = false, length = 100)
    private String course;

    @NotNull(message = "Academic year is required")
    @Min(value = 1, message = "Academic year must be at least 1")
    @Max(value = 6, message = "Academic year cannot exceed 6")
    @Column(nullable = false)
    private Integer academicYear;

    @NotNull(message = "Academic percentage is required")
    @DecimalMin(value = "0.0", message = "Percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Percentage cannot exceed 100")
    @Column(nullable = false)
    private Double academicPercentage;

    @NotNull(message = "Annual family income is required")
    @Min(value = 0, message = "Income cannot be negative")
    @Column(nullable = false)
    private Long annualFamilyIncome;

    @NotBlank(message = "State is required")
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 50)
    private String category;
}

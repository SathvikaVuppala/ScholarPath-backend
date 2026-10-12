package com.scholarpath.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scholarships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Eligibility criteria
    private Double minAcademicPercentage;   // e.g. 60.0
    private Long maxAnnualFamilyIncome;     // e.g. 250000

    @Column(length = 200)
    private String applicableCourse;        // null means all courses

    @Column(length = 100)
    private String applicableState;         // null means all states

    @Column(length = 50)
    private String applicableCategory;     // null means all categories

    @Column(columnDefinition = "TEXT")
    private String requiredDocuments;       // comma-separated list

    @Column(length = 100)
    private String deadline;               // display text e.g. "31 December 2024 (DEMO)"

    @Column(length = 500)
    private String applicationUrl;         // sample URL

    @Column(nullable = false)
    private Double amount;                 // scholarship amount in INR
}

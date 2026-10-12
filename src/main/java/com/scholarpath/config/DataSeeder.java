package com.scholarpath.config;

import com.scholarpath.entity.Scholarship;
import com.scholarpath.repository.ScholarshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with 5 illustrative DEMO scholarships if none exist.
 * These are sample records for educational/demonstration purposes only.
 * They do not represent real government schemes or actual deadlines.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ScholarshipRepository scholarshipRepository;

    @Override
    public void run(String... args) throws Exception {
        if (scholarshipRepository.count() == 0) {
            log.info("Seeding database with 5 illustrative DEMO scholarships...");
            scholarshipRepository.saveAll(getSeedScholarships());
            log.info("Seeding complete.");
        } else {
            log.info("Database already has scholarship data. Skipping seed.");
        }
    }

    private List<Scholarship> getSeedScholarships() {
        return List.of(
                Scholarship.builder()
                        .title("[DEMO] Merit Excellence Scholarship")
                        .description("A sample scholarship rewarding high academic achievers from any background. " +
                                "Demonstrates eligibility based on academic merit only. " +
                                "(This is illustrative demo data, not a real scholarship.)")
                        .minAcademicPercentage(75.0)
                        .maxAnnualFamilyIncome(null) // No income limit
                        .applicableCourse(null) // All courses
                        .applicableState(null) // All states
                        .applicableCategory(null) // All categories
                        .requiredDocuments(
                                "10th Marksheet,12th Marksheet,Latest Semester Marksheet,Bonafide Certificate,Passport Size Photograph")
                        .deadline("31 March 2025 [DEMO – Not a real deadline]")
                        .applicationUrl("http://example.com/demo-merit-scholarship")
                        .amount(25000.0)
                        .build(),

                Scholarship.builder()
                        .title("[DEMO] Need-Based Financial Aid")
                        .description("A sample scholarship for economically weaker sections. " +
                                "Demonstrates eligibility based on family income. " +
                                "(This is illustrative demo data, not a real scholarship.)")
                        .minAcademicPercentage(50.0)
                        .maxAnnualFamilyIncome(200000L) // Up to ₹2 Lakh income
                        .applicableCourse(null)
                        .applicableState(null)
                        .applicableCategory(null)
                        .requiredDocuments(
                                "Income Certificate,10th Marksheet,Aadhaar Card (Identity Proof),Bank Passbook,Bonafide Certificate")
                        .deadline("30 June 2025 [DEMO – Not a real deadline]")
                        .applicationUrl("http://example.com/demo-need-scholarship")
                        .amount(15000.0)
                        .build(),

                Scholarship.builder()
                        .title("[DEMO] Engineering Students Grant")
                        .description(
                                "A sample scholarship for engineering students to demonstrate course-based filtering. "
                                        +
                                        "(This is illustrative demo data, not a real scholarship.)")
                        .minAcademicPercentage(60.0)
                        .maxAnnualFamilyIncome(500000L) // Up to ₹5 Lakh
                        .applicableCourse("Engineering") // Only engineering courses
                        .applicableState(null)
                        .applicableCategory(null)
                        .requiredDocuments(
                                "10th Marksheet,12th Marksheet,Latest Semester Marksheet,Income Certificate,Bonafide Certificate,Passport Size Photograph")
                        .deadline("31 July 2025 [DEMO – Not a real deadline]")
                        .applicationUrl("http://example.com/demo-engineering-scholarship")
                        .amount(30000.0)
                        .build(),

                Scholarship.builder()
                        .title("[DEMO] State Topper Award – Karnataka")
                        .description(
                                "A sample scholarship for students from Karnataka to demonstrate state-based eligibility filtering. "
                                        +
                                        "(This is illustrative demo data, not a real scholarship.)")
                        .minAcademicPercentage(80.0)
                        .maxAnnualFamilyIncome(null)
                        .applicableCourse(null)
                        .applicableState("Karnataka")
                        .applicableCategory(null)
                        .requiredDocuments(
                                "10th Marksheet,12th Marksheet,Latest Semester Marksheet,Domicile Certificate,Bonafide Certificate,Passport Size Photograph")
                        .deadline("15 August 2025 [DEMO – Not a real deadline]")
                        .applicationUrl("http://example.com/demo-karnataka-scholarship")
                        .amount(20000.0)
                        .build(),

                Scholarship.builder()
                        .title("[DEMO] Minority Community Support Fund")
                        .description(
                                "A sample scholarship for students from minority communities to demonstrate category-based eligibility. "
                                        +
                                        "(This is illustrative demo data, not a real scholarship.)")
                        .minAcademicPercentage(55.0)
                        .maxAnnualFamilyIncome(300000L) // Up to ₹3 Lakh
                        .applicableCourse(null)
                        .applicableState(null)
                        .applicableCategory("Minority")
                        .requiredDocuments(
                                "Caste / Category Certificate,Income Certificate,10th Marksheet,Aadhaar Card (Identity Proof),Bank Passbook,Bonafide Certificate,Passport Size Photograph")
                        .deadline("30 September 2025 [DEMO – Not a real deadline]")
                        .applicationUrl("http://example.com/demo-minority-scholarship")
                        .amount(18000.0)
                        .build());
    }
}

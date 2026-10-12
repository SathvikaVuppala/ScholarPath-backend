package com.scholarpath.service;

import com.scholarpath.entity.Scholarship;
import com.scholarpath.entity.Student;
import com.scholarpath.repository.ScholarshipRepository;
import com.scholarpath.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final StudentRepository studentRepository;

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    public Optional<Scholarship> getScholarshipById(Long id) {
        return scholarshipRepository.findById(id);
    }

    /**
     * Rule-based eligibility check.
     * Returns a map with:
     * - status: "ELIGIBLE" | "NOT_ELIGIBLE" | "INCOMPLETE_PROFILE"
     * - reasons: list of reason strings
     */
    public Map<String, Object> checkEligibility(Long scholarshipId, Long studentId) {
        Optional<Scholarship> schOpt = scholarshipRepository.findById(scholarshipId);
        Optional<Student> stuOpt = studentRepository.findById(studentId);

        if (schOpt.isEmpty()) {
            return Map.of("status", "ERROR", "reasons", List.of("Scholarship not found."));
        }
        if (stuOpt.isEmpty()) {
            return Map.of("status", "INCOMPLETE_PROFILE",
                    "reasons", List.of("Student profile not found. Please complete your profile first."));
        }

        Scholarship s = schOpt.get();
        Student stu = stuOpt.get();
        List<String> failReasons = new ArrayList<>();
        List<String> passReasons = new ArrayList<>();

        // Check academic percentage
        if (s.getMinAcademicPercentage() != null) {
            if (stu.getAcademicPercentage() >= s.getMinAcademicPercentage()) {
                passReasons.add("Academic percentage " + stu.getAcademicPercentage()
                        + "% meets minimum requirement of " + s.getMinAcademicPercentage() + "%.");
            } else {
                failReasons.add("Academic percentage " + stu.getAcademicPercentage()
                        + "% is below the minimum requirement of " + s.getMinAcademicPercentage() + "%.");
            }
        }

        // Check annual family income
        if (s.getMaxAnnualFamilyIncome() != null) {
            if (stu.getAnnualFamilyIncome() <= s.getMaxAnnualFamilyIncome()) {
                passReasons.add("Annual family income ₹" + stu.getAnnualFamilyIncome()
                        + " is within the limit of ₹" + s.getMaxAnnualFamilyIncome() + ".");
            } else {
                failReasons.add("Annual family income ₹" + stu.getAnnualFamilyIncome()
                        + " exceeds the maximum limit of ₹" + s.getMaxAnnualFamilyIncome() + ".");
            }
        }

        // Check applicable course
        if (s.getApplicableCourse() != null && !s.getApplicableCourse().isBlank()) {
            boolean courseMatch = stu.getCourse().toLowerCase()
                    .contains(s.getApplicableCourse().toLowerCase());
            if (courseMatch) {
                passReasons.add("Course '" + stu.getCourse() + "' matches required course '"
                        + s.getApplicableCourse() + "'.");
            } else {
                failReasons.add("Course '" + stu.getCourse() + "' does not match required course '"
                        + s.getApplicableCourse() + "'.");
            }
        }

        // Check applicable state
        if (s.getApplicableState() != null && !s.getApplicableState().isBlank()) {
            boolean stateMatch = stu.getState().equalsIgnoreCase(s.getApplicableState());
            if (stateMatch) {
                passReasons.add("State '" + stu.getState() + "' matches required state '"
                        + s.getApplicableState() + "'.");
            } else {
                failReasons.add("State '" + stu.getState() + "' does not match required state '"
                        + s.getApplicableState() + "'.");
            }
        }

        // Check applicable category
        if (s.getApplicableCategory() != null && !s.getApplicableCategory().isBlank()) {
            boolean catMatch = stu.getCategory().equalsIgnoreCase(s.getApplicableCategory());
            if (catMatch) {
                passReasons.add("Category '" + stu.getCategory() + "' matches required category '"
                        + s.getApplicableCategory() + "'.");
            } else {
                failReasons.add("Category '" + stu.getCategory() + "' does not match required category '"
                        + s.getApplicableCategory() + "'.");
            }
        }

        String status = failReasons.isEmpty() ? "ELIGIBLE" : "NOT_ELIGIBLE";
        List<String> allReasons = new ArrayList<>(passReasons);
        allReasons.addAll(failReasons);

        return Map.of(
                "status", status,
                "passReasons", passReasons,
                "failReasons", failReasons,
                "allReasons", allReasons);
    }
}

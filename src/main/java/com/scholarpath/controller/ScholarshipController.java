package com.scholarpath.controller;

import com.scholarpath.entity.Scholarship;
import com.scholarpath.service.ScholarshipService;
import com.scholarpath.security.StudentPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @GetMapping
    public ResponseEntity<List<Scholarship>> getAllScholarships() {
        return ResponseEntity.ok(scholarshipService.getAllScholarships());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScholarshipById(@PathVariable Long id) {
        return scholarshipService.getScholarshipById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Check eligibility of a specific student for a specific scholarship.
     * GET /api/scholarships/{scholarshipId}/eligibility?studentId=1
     */
    @GetMapping("/{scholarshipId}/eligibility")
    public ResponseEntity<Map<String, Object>> checkEligibility(
            @PathVariable Long scholarshipId,
            @RequestParam Long studentId,
            @AuthenticationPrincipal StudentPrincipal principal) {
        if (!studentId.equals(principal.getStudentId())) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> result = scholarshipService.checkEligibility(scholarshipId, studentId);
        return ResponseEntity.ok(result);
    }

    /**
     * Check eligibility for all scholarships for a student.
     * GET /api/scholarships/eligibility-all?studentId=1
     */
    @GetMapping("/eligibility-all")
    public ResponseEntity<?> checkAllEligibility(
            @RequestParam Long studentId,
            @AuthenticationPrincipal StudentPrincipal principal) {
        if (!studentId.equals(principal.getStudentId())) {
            return ResponseEntity.notFound().build();
        }
        List<Scholarship> all = scholarshipService.getAllScholarships();
        List<Map<String, Object>> results = all.stream().map(s -> {
            Map<String, Object> elig = scholarshipService.checkEligibility(s.getId(), studentId);
            return Map.of(
                    "scholarship", s,
                    "eligibility", elig);
        }).toList();
        return ResponseEntity.ok(results);
    }
}

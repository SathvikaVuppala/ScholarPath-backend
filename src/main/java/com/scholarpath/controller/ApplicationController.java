package com.scholarpath.controller;

import com.scholarpath.entity.ScholarshipApplication;
import com.scholarpath.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ScholarshipApplication>> getApplications(@PathVariable Long studentId) {
        return ResponseEntity.ok(applicationService.getApplicationsByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<?> addApplication(@RequestBody Map<String, Long> body) {
        Long studentId = body.get("studentId");
        Long scholarshipId = body.get("scholarshipId");
        if (studentId == null || scholarshipId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "studentId and scholarshipId are required."));
        }
        try {
            ScholarshipApplication app = applicationService.addApplication(studentId, scholarshipId);
            return ResponseEntity.status(HttpStatus.CREATED).body(app);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{appId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long appId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "'status' is required."));
        }
        try {
            ScholarshipApplication updated = applicationService.updateStatus(appId, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid status. Use: NOT_STARTED, IN_PROGRESS, SUBMITTED"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long appId) {
        applicationService.deleteApplication(appId);
        return ResponseEntity.noContent().build();
    }
}

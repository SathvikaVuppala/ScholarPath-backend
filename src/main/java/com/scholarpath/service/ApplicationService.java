package com.scholarpath.service;

import com.scholarpath.entity.Scholarship;
import com.scholarpath.entity.ScholarshipApplication;
import com.scholarpath.entity.ScholarshipApplication.ApplicationStatus;
import com.scholarpath.repository.ScholarshipApplicationRepository;
import com.scholarpath.repository.ScholarshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ScholarshipApplicationRepository applicationRepository;
    private final ScholarshipRepository scholarshipRepository;

    public List<ScholarshipApplication> getApplicationsByStudentId(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Transactional
    public ScholarshipApplication addApplication(Long studentId, Long scholarshipId) {
        // Prevent duplicates
        applicationRepository.findByStudentIdAndScholarshipId(studentId, scholarshipId)
                .ifPresent(app -> {
                    throw new RuntimeException("Application already exists for this scholarship.");
                });

        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
                .orElseThrow(() -> new RuntimeException("Scholarship not found with id: " + scholarshipId));

        ScholarshipApplication app = ScholarshipApplication.builder()
                .studentId(studentId)
                .scholarship(scholarship)
                .status(ApplicationStatus.NOT_STARTED)
                .build();

        return applicationRepository.save(app);
    }

    @Transactional
    public ScholarshipApplication updateStatus(Long appId, Long studentId, String status) {
        ScholarshipApplication app = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + appId));
        if (!app.getStudentId().equals(studentId)) {
            throw new RuntimeException("Application not found with id: " + appId);
        }
        app.setStatus(ApplicationStatus.valueOf(status));
        return applicationRepository.save(app);
    }

    @Transactional
    public void deleteApplication(Long appId, Long studentId) {
        ScholarshipApplication app = applicationRepository.findById(appId)
                .filter(application -> application.getStudentId().equals(studentId))
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + appId));
        applicationRepository.delete(app);
    }
}

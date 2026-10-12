package com.scholarpath.repository;

import com.scholarpath.entity.ScholarshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScholarshipApplicationRepository extends JpaRepository<ScholarshipApplication, Long> {
    List<ScholarshipApplication> findByStudentId(Long studentId);

    Optional<ScholarshipApplication> findByStudentIdAndScholarshipId(Long studentId, Long scholarshipId);
}

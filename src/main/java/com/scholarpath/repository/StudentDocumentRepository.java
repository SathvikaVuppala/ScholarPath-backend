package com.scholarpath.repository;

import com.scholarpath.entity.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
    List<StudentDocument> findByStudentId(Long studentId);

    void deleteByStudentId(Long studentId);
}

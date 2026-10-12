package com.scholarpath.service;

import com.scholarpath.entity.StudentDocument;
import com.scholarpath.repository.StudentDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final StudentDocumentRepository documentRepository;

    public List<StudentDocument> getDocumentsByStudentId(Long studentId) {
        return documentRepository.findByStudentId(studentId);
    }

    @Transactional
    public StudentDocument updateDocument(Long docId, Long studentId, Boolean completed, String notes) {
        StudentDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + docId));
        if (!doc.getStudentId().equals(studentId)) {
            throw new RuntimeException("Document not found with id: " + docId);
        }
        doc.setCompleted(completed);
        if (notes != null)
            doc.setNotes(notes);
        return documentRepository.save(doc);
    }

    public Optional<StudentDocument> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }
}

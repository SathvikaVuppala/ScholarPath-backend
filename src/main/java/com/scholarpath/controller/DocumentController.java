package com.scholarpath.controller;

import com.scholarpath.entity.StudentDocument;
import com.scholarpath.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentDocument>> getDocumentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(documentService.getDocumentsByStudentId(studentId));
    }

    @PatchMapping("/{docId}")
    public ResponseEntity<?> updateDocument(
            @PathVariable Long docId,
            @RequestBody Map<String, Object> body) {
        Boolean completed = (Boolean) body.get("completed");
        String notes = (String) body.get("notes");
        if (completed == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "'completed' field is required."));
        }
        try {
            StudentDocument updated = documentService.updateDocument(docId, completed, notes);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

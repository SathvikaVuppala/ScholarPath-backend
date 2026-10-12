package com.scholarpath.controller;

import com.scholarpath.entity.Student;
import com.scholarpath.service.StudentService;
import com.scholarpath.security.StudentPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id, @AuthenticationPrincipal StudentPrincipal principal) {
        if (!id.equals(principal.getStudentId())) {
            return ResponseEntity.notFound().build();
        }
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<?> getStudentByEmail(
            @PathVariable String email,
            @AuthenticationPrincipal StudentPrincipal principal) {
        if (!email.equalsIgnoreCase(principal.getEmail())) {
            return ResponseEntity.notFound().build();
        }
        return studentService.getStudentByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student,
            @AuthenticationPrincipal StudentPrincipal principal) {
        if (!id.equals(principal.getStudentId())) {
            return ResponseEntity.notFound().build();
        }
        try {
            Student updated = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

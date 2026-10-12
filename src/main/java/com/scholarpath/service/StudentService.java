package com.scholarpath.service;

import com.scholarpath.entity.Student;
import com.scholarpath.entity.StudentDocument;
import com.scholarpath.repository.StudentDocumentRepository;
import com.scholarpath.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentDocumentRepository documentRepository;

    // Default document names for every new student
    private static final List<String> DEFAULT_DOCUMENTS = Arrays.asList(
            "10th Marksheet",
            "12th Marksheet",
            "Latest Semester Marksheet",
            "Income Certificate",
            "Caste / Category Certificate",
            "Aadhaar Card (Identity Proof)",
            "Passport Size Photograph",
            "Bank Passbook",
            "Bonafide Certificate",
            "Domicile Certificate");

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmailIgnoreCase(email);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public Student createStudent(Student student) {
        Student saved = studentRepository.save(student);
        ensureDocumentChecklist(saved);
        return saved;
    }

    @Transactional
    public void ensureDocumentChecklist(Student student) {
        // Seed default document checklist for new student
        if (!documentRepository.findByStudentId(student.getId()).isEmpty()) {
            return;
        }
        for (String docName : DEFAULT_DOCUMENTS) {
            StudentDocument doc = StudentDocument.builder()
                    .studentId(student.getId())
                    .documentName(docName)
                    .completed(false)
                    .build();
            documentRepository.save(doc);
        }
    }

    @Transactional
    public Student updateStudent(Long id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.findByEmailIgnoreCase(updated.getEmail())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new IllegalArgumentException("A student with this email already exists.");
                });
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        existing.setCourse(updated.getCourse());
        existing.setAcademicYear(updated.getAcademicYear());
        existing.setAcademicPercentage(updated.getAcademicPercentage());
        existing.setAnnualFamilyIncome(updated.getAnnualFamilyIncome());
        existing.setState(updated.getState());
        existing.setCategory(updated.getCategory());
        return studentRepository.save(existing);
    }

    @Transactional
    public Student registerStudent(Student student) {
        return createStudent(student);
    }
}

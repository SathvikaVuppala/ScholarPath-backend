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
        return studentRepository.findByEmail(email);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public Student createStudent(Student student) {
        Student saved = studentRepository.save(student);
        // Seed default document checklist for new student
        boolean hasDocuments = !documentRepository.findByStudentId(saved.getId()).isEmpty();
        if (!hasDocuments) {
            for (String docName : DEFAULT_DOCUMENTS) {
                StudentDocument doc = StudentDocument.builder()
                        .studentId(saved.getId())
                        .documentName(docName)
                        .completed(false)
                        .build();
                documentRepository.save(doc);
            }
        }
        return saved;
    }

    @Transactional
    public Student updateStudent(Long id, Student updated) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
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
}

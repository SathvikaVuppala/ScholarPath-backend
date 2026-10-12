package com.scholarpath.service;

import com.scholarpath.dto.RegisterRequest;
import com.scholarpath.entity.Student;
import com.scholarpath.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Student register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (studentRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new AccountAlreadyExistsException();
        }

        Student student = Student.builder()
                .fullName(request.fullName().trim())
                .email(email)
                .course(request.course())
                .academicYear(request.academicYear())
                .academicPercentage(request.academicPercentage())
                .annualFamilyIncome(request.annualFamilyIncome())
                .state(request.state())
                .category(request.category())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();
        return studentService.registerStudent(student);
    }

    public static class AccountAlreadyExistsException extends RuntimeException {
        public AccountAlreadyExistsException() {
            super("This email is already in use. Log in to the existing account or register with a different email.");
        }
    }
}

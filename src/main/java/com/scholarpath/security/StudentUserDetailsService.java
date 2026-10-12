package com.scholarpath.security;

import com.scholarpath.entity.Student;
import com.scholarpath.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));
        if (student.getPasswordHash() == null || student.getPasswordHash().isBlank()) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        return new StudentPrincipal(student.getId(), student.getEmail(), student.getPasswordHash());
    }
}

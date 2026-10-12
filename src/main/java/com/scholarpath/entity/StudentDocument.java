package com.scholarpath.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false, length = 200)
    private String documentName;

    @Column(nullable = false)
    private Boolean completed;

    @Column(length = 500)
    private String notes;
}

package com.scholarpath.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scholarship_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScholarshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scholarship_id", nullable = false)
    private Scholarship scholarship;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status;

    @Column
    private LocalDateTime appliedAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ApplicationStatus {
        NOT_STARTED, IN_PROGRESS, SUBMITTED
    }
}

package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.ReportState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(nullable = false)
    private String reportDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportState reportState = ReportState.OPEN;

    @Column(nullable = false)
    private LocalDateTime reportOpenedAt = LocalDateTime.now();

    private LocalDateTime reportClosedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "student_id")
    private Student author;
}

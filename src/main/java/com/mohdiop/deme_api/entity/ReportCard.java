package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.ReportCardResponse;
import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(
        name = "report_cards",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "report_card_emitted_at"})
        }
)
public class ReportCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportCardId;

    @Column(nullable = false)
    private String reportCardUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentLevel reportCardLevel;

    @Column(nullable = false)
    private LocalDateTime reportCardCreatedAt;

    @Column(nullable = false)
    private LocalDate reportCardEmittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType reportCardFileType;

    @Column(nullable = false)
    private Double reportCardFileSizeMo;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public ReportCardResponse toResponse() {
        return new ReportCardResponse(
                reportCardId,
                student.getUserId(),
                reportCardUrl,
                reportCardLevel,
                reportCardFileType,
                reportCardFileSizeMo,
                reportCardEmittedAt,
                reportCardCreatedAt
        );
    }
}

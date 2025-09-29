package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.ReportResponse;
import com.mohdiop.deme_api.entity.enumeration.ReportState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "student_id")
    private Student author;

    public ReportResponse toResponse() {
        return new ReportResponse(
                reportId,
                author.getUserId(),
                expense.getExpenseId(),
                reportDescription,
                reportState,
                reportOpenedAt,
                reportClosedAt
        );
    }
}

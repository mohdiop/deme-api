package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.MakeReportRequest;
import com.mohdiop.deme_api.dto.response.ReportResponse;
import com.mohdiop.deme_api.entity.Expense;
import com.mohdiop.deme_api.entity.Report;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.ReportState;
import com.mohdiop.deme_api.repository.ExpenseRepository;
import com.mohdiop.deme_api.repository.ReportRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final StudentRepository studentRepository;
    private final ExpenseRepository expenseRepository;

    public ReportService(ReportRepository reportRepository, StudentRepository studentRepository, ExpenseRepository expenseRepository) {
        this.reportRepository = reportRepository;
        this.studentRepository = studentRepository;
        this.expenseRepository = expenseRepository;
    }

    public ReportResponse makeReport(
            Long authorId,
            Long expenseId,
            MakeReportRequest makeReportRequest
    ) throws BadRequestException {
        Student author = studentRepository.findById(authorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Dépense introuvable.")
                );
        if (reportRepository.findByExpenseExpenseId(expenseId).isPresent()) {
            throw new BadRequestException("Cette dépense a déjà été signalée.");
        }
        Report report = makeReportRequest.toReport();
        report.setAuthor(author);
        report.setExpense(expense);
        return reportRepository.save(report).toResponse();
    }

    public ReportResponse setReportState(
            Long reportId,
            ReportState reportState
    ) throws BadRequestException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Signalement introuvable.")
                );
        if (report.getReportState() == ReportState.CLOSE) {
            throw new BadRequestException("Signalement fermé.");
        }
        if (reportState == ReportState.CLOSE) {
            if (report.getReportState() != ReportState.OPEN) {
                throw new BadRequestException("Seul un signalement ouvert peut être fermé.");
            }
        }
        report.setReportState(reportState);
        return reportRepository.save(report).toResponse();
    }

    public List<ReportResponse> getAllReports() {
        var allReports = reportRepository.findAll();
        if (allReports.isEmpty()) return new ArrayList<>();
        return allReports.stream().map(Report::toResponse).toList();
    }
}

package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByExpenseExpenseId(Long expenseId);
}

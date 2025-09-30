package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.ReportCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {
    Optional<ReportCard> findByStudentUserIdAndReportCardEmittedAt(Long studentId, LocalDate emittedAt);
}

package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}

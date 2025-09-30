package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReportCardResponse(
        Long id,
        Long studentId,
        String url,
        StudentLevel level,
        FileType type,
        Double sizeMo,
        LocalDate emittedAt,
        LocalDateTime createdAt
) {
}

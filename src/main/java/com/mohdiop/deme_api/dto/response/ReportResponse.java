package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.ReportState;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        Long authorId,
        Long expenseId,
        String description,
        ReportState state,
        LocalDateTime openedAt,
        LocalDateTime closedAt
) {
}

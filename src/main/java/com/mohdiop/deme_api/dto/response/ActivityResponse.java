package com.mohdiop.deme_api.dto.response;

import java.time.LocalDateTime;

public record ActivityResponse(
        Long id,
        Long studentId,
        String name,
        String description,
        String studentWords,
        LocalDateTime createdAt,
        LocalDateTime doneAt,
        ProofResponse proof
) {
}

package com.mohdiop.deme_api.dto.response;

import java.time.LocalDateTime;

public record ArgumentResponse(
        Long id,
        Long authorId,
        String body,
        LocalDateTime madeAt,
        ProofResponse proof
) {
}

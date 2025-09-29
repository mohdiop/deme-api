package com.mohdiop.deme_api.dto.response;

import java.time.LocalDateTime;

public record ExpenseResponse(
        Long id,

        Long sponsorshipId,

        Long needId,

        String name,

        String description,

        Double amount,

        LocalDateTime madeAt,

        ProofResponse proof
) {
}

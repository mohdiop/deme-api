package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.PaymentType;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,

        String paymentTransactionId,

        Long sponsorshipId,

        PaymentType type,

        LocalDateTime date,

        Double amount,

        String currency,

        Double equivalenceXOF
) {
}

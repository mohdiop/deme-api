package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.TransferState;

import java.time.LocalDateTime;

public record FundsTransferResponse(
        Long id,
        Long authorId,
        Long fromStudent,
        Long toStudent,
        Double amount,
        LocalDateTime askedAt,
        LocalDateTime acceptedAt,
        TransferState state
) {
}

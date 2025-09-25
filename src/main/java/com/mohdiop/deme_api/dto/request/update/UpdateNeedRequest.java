package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.NeedEmergency;
import com.mohdiop.deme_api.entity.enumeration.NeedType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UpdateNeedRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "La description ne doit pas être vide."
        )
        String description,

        @DecimalMin(value = "5000.00", message = "Le montant doit être supérieur ou égal à 5000 XOF.")
        @DecimalMax(value = "500000.00", message = "Le montant ne peut pas dépasser 500000 XOF.")
        Double amount,

        NeedType type,

        NeedEmergency emergency,

        @Future(message = "La date d'expiration doit être dans le futur.")
        LocalDateTime expiresAt
) {
}

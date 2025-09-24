package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Need;
import com.mohdiop.deme_api.entity.enumeration.NeedEmergency;
import com.mohdiop.deme_api.entity.enumeration.NeedState;
import com.mohdiop.deme_api.entity.enumeration.NeedType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateNeedRequest(
        @NotBlank(message = "La description est obligatoire et ne doit pas être vide.") String description,
        @NotNull(message = "Le montant est obligatoire.")
        @DecimalMin(value = "5000.00", message = "Le montant doit être supérieur ou égal à 5000 XOF.")
        @DecimalMax(value = "500000.00", message = "Le montant ne peut pas dépasser 500000 XOF.")
        @PositiveOrZero(message = "Le montant ne peut pas être négatif.")
        Double amount,
        @NotNull(message = "Type de besoin obligatoire.") NeedType type,
        @NotNull(message = "Urgence de besoin obligatoire.") NeedEmergency emergency,
        @NotNull(message = "La date d'expiration est obligatoire.")
        @Future(message = "La date d'expiration doit être dans le future.") LocalDateTime expiresAt
) {
    public Need toStudentlessNeed() {
        return Need.builder()
                .needDescription(description)
                .neededAmount(amount)
                .needType(type)
                .needEmergency(emergency)
                .needCreatedAt(LocalDateTime.now())
                .needState(NeedState.UNSATISFIED)
                .needExpiresAt(expiresAt)
                .build();
    }
}

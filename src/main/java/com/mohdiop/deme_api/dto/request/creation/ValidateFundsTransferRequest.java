package com.mohdiop.deme_api.dto.request.creation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ValidateFundsTransferRequest(
        @NotNull(message = "L'identifiant de l'élève destinataire est obligatoire.")
        @Positive(message = "Identifiant invalide.")
        Long toStudent
) {
}

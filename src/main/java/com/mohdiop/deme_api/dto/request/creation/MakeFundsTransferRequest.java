package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.FundsTransfer;
import com.mohdiop.deme_api.entity.enumeration.TransferState;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record MakeFundsTransferRequest(
        @NotNull(message = "L'identifiant de l'élève expéditeur est obligatoire.")
        @Positive(message = "Identifiant invalide.")
        Long fromStudent,

        @NotNull(message = "Le montant est obligatoire.")
        @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0.")
        Double amount
) {

    public FundsTransfer toFundsTransfer() {
        return FundsTransfer.builder()
                .transferId(null)
                .transferAmount(amount)
                .transferState(TransferState.PENDING)
                .transferAskedAt(LocalDateTime.now())
                .transferAcceptedAt(null)
                .build();
    }
}

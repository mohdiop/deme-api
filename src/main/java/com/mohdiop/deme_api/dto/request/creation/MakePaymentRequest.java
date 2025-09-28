package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Payment;
import com.mohdiop.deme_api.entity.enumeration.PaymentType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record MakePaymentRequest(
        @NotNull(message = "L'identifiant du parrainage est obligatoire.")
        @Positive(message = "Identifiant invalide.") Long sponsorshipId,

        @NotBlank(message = "L'identifiant de la transaction de paiement est obligatoire et ne doit pas être vide.")
        String transactionId,

        @NotNull(message = "Le type de paiement est obligatoire.")
        PaymentType type,

        @NotNull(message = "Le montant du paiement est obligatoire.")
        @DecimalMin(value = "0.01", message = "Le montant du paiement doit être supérieur à 0.")
        Double amount,

        @NotBlank(message = "La devise du paiement est obligatoire et ne doit pas être vide.")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "La devise doit être un code ISO 4217 valide (ex: XOF, EUR, USD)."
        )
        String currency,

        @NotNull(message = "L'équivalent en XOF est obligatoire.")
        @DecimalMin(value = "0.01", message = "L'équivalent en XOF doit être supérieur à 0.")
        Double equivalenceXOF
) {

    public Payment toSonsorshiplessPayment() {
        return Payment.builder()
                .paymentId(null)
                .paymentTransactionId(transactionId)
                .paymentType(type)
                .paymentAmount(amount)
                .paymentCurrency(currency)
                .paymentDate(LocalDateTime.now())
                .paymentEquivalenceXOF(equivalenceXOF)
                .build();
    }
}

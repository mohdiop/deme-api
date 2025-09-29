package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Expense;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record CreateExpenseRequest(
        @NotNull(message = "L'identifiant du besoin est obligatoire.")
        @Positive(message = "Identifiant du besoin invalide.")
        Long needId,

        @NotBlank(message = "Le nom est obligatoire et ne doit pas être vide.")
        String name,

        @NotBlank(message = "La description est obligatoire et ne doit pas être vide.")
        String description,

        @NotNull(message = "Le montant est obligatoire.")
        @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0.")
        Double amount,

        @NotNull(message = "La preuve de paiement est obligatoire.")
        MultipartFile proof
) {
    public Expense toExpense() {
        return Expense.builder()
                .expenseId(null)
                .expenseName(name)
                .expenseDescription(description)
                .expenseAmount(amount)
                .expenseMadeAt(LocalDateTime.now())
                .build();
    }
}

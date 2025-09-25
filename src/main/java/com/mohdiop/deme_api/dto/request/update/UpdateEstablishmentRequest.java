package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.EstablishmentLevel;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record UpdateEstablishmentRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le nom de l'établissement ne doit pas être vide."
        )
        String name,

        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "L'adresse ne doit pas être vide."
        )
        String address,

        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le numéro d'identification ne doit pas être vide."
        )
        String identificationNumber,

        @PastOrPresent(message = "La date de création doit être dans le passé ou aujourd'hui.")
        LocalDateTime creationDate,

        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phone,

        @Email(message = "Email invalide.")
        String email,

        EstablishmentType type,

        @Size(min = 1, message = "Au moins un niveau d'établissement doit être spécifié.")
        Set<@NotNull EstablishmentLevel> levels
) {
}

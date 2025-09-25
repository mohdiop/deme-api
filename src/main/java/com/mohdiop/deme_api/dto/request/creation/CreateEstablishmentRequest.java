package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Establishment;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentLevel;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record CreateEstablishmentRequest(
        @NotBlank(message = "Le nom de l'établissement est obligatoire et ne doit pas être vide.")
        String name,

        @NotBlank(message = "L'adresse est obligatoire et ne doit pas être vide.")
        String address,

        @NotBlank(message = "Le numéro d'identification est obligatoire et ne doit pas être vide.")
        String identificationNumber,

        @NotNull(message = "La date de création est obligatoire.")
        @PastOrPresent(message = "La date de création doit être dans le passé ou aujourd'hui.")
        LocalDateTime creationDate,

        @NotBlank(message = "Le numéro de téléphone est obligatoire.")
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phone,

        @NotBlank(message = "L'email est obligatoire.")
        @Email(message = "Email invalide.")
        String email,

        @NotNull(message = "Le type d'établissement est obligatoire.")
        EstablishmentType type,

        @NotNull(message = "Le niveau d'établissement est obligatoire.")
        @Size(min = 1, message = "Au moins un niveau d'établissement doit être spécifié.")
        Set<@NotNull EstablishmentLevel> levels
) {

    public Establishment toEstablishment() {
        LocalDateTime now = LocalDateTime.now();
        return Establishment.builder()
                .establishmentId(null)
                .establishmentName(name)
                .establishmentPhoneNumber(phone)
                .establishmentEmail(email)
                .establishmentAddress(address)
                .establishmentIdentificationNumber(identificationNumber)
                .establishmentType(type)
                .establishmentLevels(levels)
                .establishmentCreationDate(creationDate)
                .createdAt(now)
                .lastUpdatedAt(now)
                .students(new HashSet<>())
                .build();
    }
}

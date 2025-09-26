package com.mohdiop.deme_api.dto.request.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateTutorRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le prénom ne doit pas être vide."
        )
        String firstName,

        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le nom de famille ne doit pas être vide."
        )
        String lastName,

        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phoneNumber,

        @Email(message = "Email invalide.")
        String email,

        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "L'adresse ne doit pas être vide."
        )
        String address
) {
}

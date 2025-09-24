package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateSchoolRequest(
        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "Nom d'école ne peut pas être vide."
        )
        String name,
        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "Numéro d'identification ne peut pas être vide."
        )
        String identificationNumber,
        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "Adresse ne peut pas être vide."
        )
        String address,
        SchoolType type, // rendu optionnel
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phone,
        @Email(message = "Email invalide.")
        String email,
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "Mot de passe invalide (min 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial)"
        )
        String password
) {
}

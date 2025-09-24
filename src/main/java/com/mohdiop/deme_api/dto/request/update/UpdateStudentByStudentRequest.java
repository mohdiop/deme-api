package com.mohdiop.deme_api.dto.request.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateStudentByStudentRequest(
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

package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.OrganizationLegalStatus;
import com.mohdiop.deme_api.entity.enumeration.OrganizationTargetAudience;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record UpdateOrganizationRequest(

        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "Le nom ne doit pas être vide."
        )
        String name,

        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "L'adresse ne doit pas être vide."
        )
        String address,


        OrganizationLegalStatus legalStatus,

        @PastOrPresent(message = "La date de création doit être dans le passé ou aujourd'hui.")
        LocalDateTime createdAt,

        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "L'historique ne doit pas être vide."
        )
        String history,

        @Pattern(
                regexp = "^\\s*\\S.*$",
                message = "La zone d'intervention ne doit pas être vide."
        )
        String interventionArea,

        @Size(min = 1, message = "Au moins un public cible doit être sélectionné si fourni.")
        Set<@NotNull OrganizationTargetAudience> targetAudiences,

        @Pattern(
                regexp = "^https?://.*$",
                message = "Le site web doit être une URL valide (commençant par http:// ou https://)."
        )
        String webSite,

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
        String password,

        @Pattern(
                regexp = "^https?://.*$",
                message = "L'URL de l'image doit être valide (commençant par http:// ou https://)."
        )
        String pictureUrl
) {
}

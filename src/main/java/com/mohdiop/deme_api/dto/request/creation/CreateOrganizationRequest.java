package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Organization;
import com.mohdiop.deme_api.entity.enumeration.OrganizationLegalStatus;
import com.mohdiop.deme_api.entity.enumeration.OrganizationTargetAudience;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreateOrganizationRequest(

        @NotBlank(message = "Le nom est obligatoire et ne doit pas être vide.")
        String name,

        @NotBlank(message = "L'adresse est obligatoire et ne doit pas être vide.")
        String address,

        @NotNull(message = "Le statut juridique de l'organisation est obligatoire.")
        OrganizationLegalStatus legalStatus,

        @NotNull(message = "La date de création est obligatoire.")
        @PastOrPresent(message = "La date de création doit être dans le passé ou aujourd'hui.")
        LocalDateTime createdAt,

        @NotBlank(message = "L'historique est obligatoire et ne doit pas être vide.")
        String history,

        @NotBlank(message = "La zone d'intervention est obligatoire et ne doit pas être vide.")
        String interventionArea,

        @NotNull(message = "Au moins un public cible doit être spécifié.")
        @Size(min = 1, message = "Au moins un public cible doit être sélectionné.")
        Set<@NotNull OrganizationTargetAudience> targetAudiences,

        @Pattern(
                regexp = "^https?://.*$",
                message = "Le site web doit être une URL valide (commençant par http:// ou https://)."
        )
        String webSite,

        @NotBlank(message = "Le numéro de téléphone est obligatoire.")
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phone,

        @NotBlank(message = "L'email est obligatoire.")
        @Email(message = "Email invalide.")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire.")
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
    public Organization toOrganization() {
        return Organization.builder()
                .id(null)
                .name(name)
                .address(address)
                .legalStatus(legalStatus)
                .createdAt(createdAt)
                .history(history)
                .interventionArea(interventionArea)
                .targetAudiences(targetAudiences)
                .organizationWebSite(webSite)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .pictureUrl(pictureUrl)
                .roles(new HashSet<>(List.of(UserRole.ROLE_ORGANIZATION)))
                .state(UserState.ACTIVE)
                .build();
    }
}

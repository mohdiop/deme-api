package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;

public record CreateSponsorRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le prénom ne doit pas être vide."
        ) String firstName,
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le nom de famille ne doit pas être vide."
        ) String lastName,
        @NotNull(message = "Type de sponsor obligatoire.") SponsorType sponsorType,
        String organizationName,
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "L'adresse ne doit pas être vide."
        ) String address,
        @NotNull(message = "Le numéro de téléphone est obligatoire.")
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        ) String phone,
        @Email(message = "Email invalide.") String email,
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Mot de passe invalide (min 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial)"
        ) String password
) {
    public Sponsor toSponsor() {
        return Sponsor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .sponsorType(sponsorType)
                .organizationName(organizationName)
                .address(address)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_SPONSOR)))
                .state(UserState.ACTIVE)
                .build();
    }
}

package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;

public record CreateSchoolRequest(
        @NotBlank(message = "Nom d'école obligatoire et ne peut pas être vide.") String name,
        @NotBlank(message = "Numéro d'identification obligatoire et ne peut pas être vide.") String identificationNumber,
        @NotBlank(message = "Adresse obligatoire et ne peut pas être vide.") String address,
        @NotNull(message = "Type d'école obligatoire et ne peut pas être vide.") SchoolType type,
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
    public School toSchool() {
        return School.builder()
                .name(name)
                .identificationNumber(identificationNumber)
                .address(address)
                .type(type)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_SCHOOL)))
                .state(UserState.ACTIVE)
                .build();
    }
}

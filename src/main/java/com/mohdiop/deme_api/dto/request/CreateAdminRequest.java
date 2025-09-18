package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.Admin;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;

public record CreateAdminRequest(
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
    public Admin toEntity() {
        return Admin.builder()
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_ADMIN)))
                .state(UserState.ACTIVE)
                .build();
    }
}

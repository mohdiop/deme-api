package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public record CreateStudentRequest(
        @NotBlank(message = "Le prénom est obligatoire et ne doit pas être vide.") String firstName,
        @NotBlank(message = "Le nom de famille est obligatoire et ne doit pas être vide.") String lastName,
        @NotNull(message = "La date de naissance est obligatoire.")
        @Past(message = "La date de naissance doit être dans le passé.")
        LocalDateTime bornOn,
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "Le lieu de naissance ne doit pas être vide."
        ) String bornAt,
        @NotNull(message = "Le sexe est obligatoire.") StudentGender gender,
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "L'adresse ne doit pas être vide."
        ) String address,
        @NotNull(message = "Le niveau est obligatoire.") StudentLevel level,
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "La spécialité ne doit pas être vide."
        )
        String speciality,
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
    public Student toSchoollessStudent() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .bornOn(bornOn)
                .bornAt(bornAt)
                .gender(gender)
                .address(address)
                .level(level)
                .speciality(speciality)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_STUDENT)))
                .state(UserState.ACTIVE)
                .funds(0D)
                .build();
    }
}

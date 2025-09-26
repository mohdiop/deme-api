package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Tutor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateTutorRequest(
        @NotBlank(message = "Le prénom est obligatoire et ne doit pas être vide.")
        String firstName,

        @NotBlank(message = "Le nom de famille est obligatoire et ne doit pas être vide.")
        String lastName,

        @NotBlank(message = "Le numéro de téléphone est obligatoire.")
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Numéro de téléphone invalide (Ex: +22370000000)."
        )
        String phoneNumber,

        @Email(message = "Email invalide.")
        String email,

        @NotBlank(message = "L'adresse est obligatoire et ne doit pas être vide.")
        String address
) {

    public Tutor toStudentlessTutor() {
        return Tutor.builder()
                .tutorId(null)
                .tutorFirstName(firstName)
                .tutorLastName(lastName)
                .tutorPhone(phoneNumber)
                .tutorEmail(email)
                .tutorAddress(address)
                .build();
    }
}

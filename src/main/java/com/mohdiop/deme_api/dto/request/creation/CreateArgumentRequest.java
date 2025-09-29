package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Argument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record CreateArgumentRequest(
        @NotBlank(message = "Le corps de l'argument est obligatoire et ne doit pas être vide.")
        String body,

        MultipartFile proof,

        @Positive(message = "Identifiant invalide.")
        Long proofId
) {
    public Argument toArgument() {
        return Argument.builder()
                .argumentId(null)
                .argumentBody(body)
                .argumentDate(LocalDateTime.now())
                .build();
    }
}

package com.mohdiop.deme_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "Email invalide.") String email,
        @NotBlank(message = "Mot de passe invalide.") String password
) {
}

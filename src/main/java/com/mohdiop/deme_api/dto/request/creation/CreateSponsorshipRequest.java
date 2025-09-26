package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateSponsorshipRequest(
        @NotNull(message = "L'identifiant de l'étudiant est obligatoire.")
        @Positive(message = "Identifiant de l'étudiant invalide.")
        Long studentId,

        @NotNull(message = "La date de fin est obligatoire.")
        @Future(message = "La date de fin doit être dans le futur.")
        LocalDateTime endAt,

        @NotNull(message = "Le type de parrainage est obligatoire.")
        SponsorshipType type
) {
    public Sponsorship toStudentlessAndSponsorlessSponsorship() {
        return Sponsorship.builder()
                .sponsorshipId(null)
                .sponsorshipStartedAt(LocalDateTime.now())
                .sponsorshipEndAt(endAt)
                .sponsorshipType(type)
                .sponsorshipState(SponsorshipState.IN_PROGRESS)
                .build();
    }
}

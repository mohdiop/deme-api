package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.SponsorshipType;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record UpdateSponsorshipRequest(
        @Future(message = "La date de fin doit être dans le futur.")
        LocalDateTime endAt,

        SponsorshipType type
) {
}

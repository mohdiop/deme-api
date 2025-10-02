package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipType;

import java.time.LocalDateTime;

public record SponsorshipResponse(
        Long id,
        Long sponsorId,
        Long studentId,
        LocalDateTime startedAt,
        LocalDateTime endAt,
        SponsorshipType type,
        SponsorshipState state,
        Boolean studentInfoAccessible
) {
}

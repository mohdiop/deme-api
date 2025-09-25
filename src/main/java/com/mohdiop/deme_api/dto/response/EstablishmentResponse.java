package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.EstablishmentLevel;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentType;

import java.time.LocalDateTime;
import java.util.Set;

public record EstablishmentResponse(
        Long id,
        String name,
        String address,
        String identificationNumber,
        String phone,
        String email,
        Set<EstablishmentLevel> levels,
        EstablishmentType type,
        LocalDateTime creationDate,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}

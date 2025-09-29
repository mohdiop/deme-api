package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.NeedEmergency;
import com.mohdiop.deme_api.entity.enumeration.NeedState;
import com.mohdiop.deme_api.entity.enumeration.NeedType;

import java.time.LocalDateTime;

public record NeedResponse(
        Long id,
        Long studentId,
        String description,
        Double amount,
        NeedType type,
        NeedEmergency emergency,
        NeedState needState,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}

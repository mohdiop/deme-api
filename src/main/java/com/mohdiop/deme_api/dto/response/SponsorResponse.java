package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.Set;

public record SponsorResponse(
        Long id,
        String phone,
        String email,
        Set<UserRole> roles,
        UserState state,
        String firstName,
        String lastName,
        SponsorType sponsorType,
        String organizationName,
        String address,
        LocalDateTime createdAt
) {
}

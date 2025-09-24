package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.OrganizationLegalStatus;
import com.mohdiop.deme_api.entity.enumeration.OrganizationTargetAudience;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.Set;

public record OrganizationResponse(
        Long id,
        String name,
        OrganizationLegalStatus legalStatus,
        LocalDateTime organizationCreatedAt,
        String history,
        String interventionArea,
        Set<OrganizationTargetAudience> targetAudiences,
        String webSite,
        String phone,
        String email,
        String pictureUrl,
        Set<UserRole> roles,
        UserState state,
        LocalDateTime accountCreatedAt
) {
}

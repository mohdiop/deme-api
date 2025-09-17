package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.Set;

public record AdminResponse(
        Long id,
        String phone,
        String email,
        Set<UserRole> roles,
        UserState state,
        LocalDateTime createdAt
) {
}

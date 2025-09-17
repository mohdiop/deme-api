package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.Set;

public record SchoolResponse(
        Long id,
        String name,
        String identificationNumber,
        String address,
        SchoolType type,
        String phone,
        String email,
        Set<UserRole> roles,
        UserState state,
        LocalDateTime createdAt
) {
}

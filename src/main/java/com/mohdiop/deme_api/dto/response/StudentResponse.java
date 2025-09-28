package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.Set;

public record StudentResponse(
        Long id,
        Long organizationId,
        Long establishmentId,
        String firstName,
        String lastName,
        LocalDateTime bornOn,
        String bornAt,
        StudentGender gender,
        String address,
        StudentLevel level,
        String speciality,
        String phone,
        String email,
        Set<UserRole> roles,
        UserState state,
        LocalDateTime createdAt,
        Double funds
) {
}

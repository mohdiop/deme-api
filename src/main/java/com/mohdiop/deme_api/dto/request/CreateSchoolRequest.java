package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreateSchoolRequest(
        String name,
        String identificationNumber,
        String address,
        SchoolType type,
        String phone,
        String email,
        String password,
        Set<UserRole> roles,
        UserState state
) {
    public School toSchool() {
        return School.builder()
                .name(name)
                .identificationNumber(identificationNumber)
                .address(address)
                .type(type)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_SCHOOL)))
                .state(state)
                .build();
    }
}

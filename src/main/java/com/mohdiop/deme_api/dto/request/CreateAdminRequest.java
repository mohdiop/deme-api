package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.Admin;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Set;

public record CreateAdminRequest(
        String phone,
        String email,
        String password,
        Set<UserRole> roles,
        UserState state
) {
    public Admin toEntity() {
        return Admin.builder()
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(roles)
                .state(state)
                .build();
    }
}

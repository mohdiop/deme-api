package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.Admin;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;

import java.util.Set;

public record CreateAdminRequest(
        String phone, String email, String password, Set<UserRole> roles, UserState state
) {
    public Admin toAdmin() {
        return Admin.builder()
                .id(0L)
                .phone(phone)
                .email(email)
                .password(password)
                .roles(roles)
                .state(state)
                .build();
    }
}

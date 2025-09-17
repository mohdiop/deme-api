package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreateSponsorRequest(
        String firstName,
        String lastName,
        SponsorType sponsorType,
        String organizationName,
        String address,
        String phone,
        String email,
        String password,
        Set<UserRole> roles,
        UserState state
) {
    public Sponsor toSponsor() {
        return Sponsor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .sponsorType(sponsorType)
                .organizationName(organizationName)
                .address(address)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_SPONSOR)))
                .state(UserState.ACTIVE)
                .build();
    }
}

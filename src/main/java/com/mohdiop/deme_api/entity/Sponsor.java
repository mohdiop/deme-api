package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "sponsors")
public class Sponsor extends User {

    private String sponsorFirstName;

    private String sponsorLastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SponsorType sponsorType;

    private String sponsorOrganizationName;

    private String sponsorAddress;

    @Builder
    public Sponsor(
            Long id,
            String phone,
            String email,
            String password,
            Set<UserRole> roles,
            UserState state,
            String firstName,
            String lastName,
            SponsorType sponsorType,
            String organizationName,
            String address
    ) {
        super(id, phone, email, password, roles, LocalDateTime.now(), state);
        this.sponsorFirstName = firstName;
        this.sponsorLastName = lastName;
        this.sponsorType = sponsorType;
        this.sponsorOrganizationName = organizationName;
        this.sponsorAddress = address;
    }

    public SponsorResponse toResponse() {
        return new SponsorResponse(
                getUserId(),
                getUserPhone(),
                getUserEmail(),
                getUserRoles(),
                getUserState(),
                sponsorFirstName,
                sponsorLastName,
                sponsorType,
                sponsorOrganizationName,
                sponsorAddress,
                getUserCreatedAt()
        );
    }
}

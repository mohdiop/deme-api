package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.OrganizationResponse;
import com.mohdiop.deme_api.entity.enumeration.OrganizationLegalStatus;
import com.mohdiop.deme_api.entity.enumeration.OrganizationTargetAudience;
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
@Table(name = "organizations")
public class Organization extends User {

    @Column(nullable = false)
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationLegalStatus organizationLegalStatus;

    @Column(nullable = false)
    private LocalDateTime organizationCreatedAt;

    @Column(nullable = false)
    private String organizationHistory;

    @Column(nullable = false)
    private String organizationAddress;

    @Column(nullable = false)
    private String organizationInterventionArea;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "organization_target_audiences",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "target_audience")
    private Set<OrganizationTargetAudience> organizationTargetAudiences;

    private String organizationWebSite;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<Student> students;

    @Builder
    public Organization(
            Long id,
            String name,
            OrganizationLegalStatus legalStatus,
            LocalDateTime createdAt,
            String address,
            String history,
            String interventionArea,
            Set<OrganizationTargetAudience> targetAudiences,
            String organizationWebSite,
            String phone,
            String email,
            String password,
            String pictureUrl,
            Set<UserRole> roles,
            UserState state
    ) {
        super(id, phone, email, password, pictureUrl, roles, LocalDateTime.now(), state);
        this.organizationName = name;
        this.organizationAddress = address;
        this.organizationLegalStatus = legalStatus;
        this.organizationCreatedAt = createdAt;
        this.organizationHistory = history;
        this.organizationInterventionArea = interventionArea;
        this.organizationTargetAudiences = targetAudiences;
        this.organizationWebSite = organizationWebSite;
    }

    public OrganizationResponse toResponse() {
        return new OrganizationResponse(
                getUserId(),
                organizationName,
                organizationLegalStatus,
                organizationCreatedAt,
                organizationHistory,
                organizationInterventionArea,
                organizationTargetAudiences,
                organizationWebSite,
                getUserPhone(),
                getUserEmail(),
                getPictureUrl(),
                getUserRoles(),
                getUserState(),
                getUserCreatedAt()
        );
    }
}

package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "sponsors")
public class Sponsor extends User {

    private String sponsorFirstName;

    private String sponsorLastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SponsorType sponsorType;

    private String sponsorOrganizationName;

    private String sponsorAddress;
}

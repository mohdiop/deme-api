package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.SponsorshipResponse;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sponsorships")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sponsorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sponsorshipId;

    @Column(nullable = false)
    private LocalDateTime sponsorshipStartedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime sponsorshipEndAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SponsorshipType sponsorshipType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SponsorshipState sponsorshipState;

    @ManyToOne
    @JoinColumn(name = "sponsor_id", nullable = false)
    private Sponsor sponsor;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public SponsorshipResponse toResponse() {
        return new SponsorshipResponse(
                sponsorshipId,
                sponsor.getUserId(),
                student.getUserId(),
                sponsorshipStartedAt,
                sponsorshipEndAt,
                sponsorshipType,
                sponsorshipState
        );
    }
}

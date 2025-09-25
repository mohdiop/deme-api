package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.EstablishmentResponse;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentLevel;
import com.mohdiop.deme_api.entity.enumeration.EstablishmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "establishments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long establishmentId;

    @Column(nullable = false, unique = true)
    private String establishmentName;

    @Column(nullable = false)
    private String establishmentAddress;

    @Column(nullable = false)
    private String establishmentIdentificationNumber;

    @Column(nullable = false, unique = true)
    private String establishmentPhoneNumber;

    @Column(nullable = false, unique = true)
    private String establishmentEmail;

    @Column(nullable = false)
    private LocalDateTime establishmentCreationDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "establishment_levels",
            joinColumns = @JoinColumn(name = "establishment_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<EstablishmentLevel> establishmentLevels;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstablishmentType establishmentType;

    @OneToMany(cascade = CascadeType.ALL)
    @Transient
    private Set<Student> students;

    public EstablishmentResponse toResponse() {
        return new EstablishmentResponse(
                establishmentId,
                establishmentName,
                establishmentAddress,
                establishmentIdentificationNumber,
                establishmentPhoneNumber,
                establishmentEmail,
                establishmentLevels,
                establishmentType,
                establishmentCreationDate,
                createdAt,
                lastUpdatedAt
        );
    }
}

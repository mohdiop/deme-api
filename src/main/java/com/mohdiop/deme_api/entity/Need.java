package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.NeedResponse;
import com.mohdiop.deme_api.entity.enumeration.NeedEmergency;
import com.mohdiop.deme_api.entity.enumeration.NeedState;
import com.mohdiop.deme_api.entity.enumeration.NeedType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "needs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Need {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long needId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private String needDescription;

    @Column(nullable = false)
    private Double neededAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NeedType needType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NeedEmergency needEmergency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeedState needState;

    @Column(nullable = false)
    private LocalDateTime needCreatedAt;

    @Column(nullable = false)
    private LocalDateTime needExpiresAt;

    public NeedResponse toResponse() {
        return new NeedResponse(
                needId,
                student.getUserId(),
                needDescription,
                neededAmount,
                needType,
                needEmergency,
                needState,
                needCreatedAt,
                needExpiresAt
        );
    }
}

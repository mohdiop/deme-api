package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.ProofResponse;
import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "proofs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Proof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proofId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType proofFileType;

    @Column(nullable = false)
    private String proofUrl;

    @Column(nullable = false)
    private LocalDateTime proofUploadedAt = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProofType proofType;

    public ProofResponse toResponse() {
        return new ProofResponse(
                proofId,
                proofFileType,
                proofType,
                proofUrl,
                proofUploadedAt
        );
    }
}

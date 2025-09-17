package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "proofs")
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
}

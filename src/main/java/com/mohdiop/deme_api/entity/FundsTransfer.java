package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.TransferState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "funds_transfers")
public class FundsTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "organization_id")
    private Organization author;

    @OneToOne
    @JoinColumn(name = "from_student_id", nullable = false)
    private Student fromStudent;

    @OneToOne
    @JoinColumn(name = "to_student_id")
    private Student toStudent;

    @Column(nullable = false)
    private Double transferAmount;

    @Column(nullable = false)
    private LocalDateTime transferAskedAt = LocalDateTime.now();

    private LocalDateTime transferAcceptedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferState transferState;
}

package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private String paymentTransactionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(nullable = false)
    private Double paymentAmount;

    @Column(nullable = false)
    private String paymentCurrency;

    @Column(nullable = false)
    private Double paymentEquivalenceXOF;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sponsorship_id", nullable = false)
    private Sponsorship sponsorship;
}

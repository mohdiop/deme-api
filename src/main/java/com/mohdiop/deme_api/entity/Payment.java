package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.PaymentResponse;
import com.mohdiop.deme_api.entity.enumeration.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public PaymentResponse toResponse() {
        return
                new PaymentResponse(
                        paymentId,
                        paymentTransactionId,
                        sponsorship.getSponsorshipId(),
                        paymentType,
                        paymentDate,
                        paymentAmount,
                        paymentCurrency,
                        paymentEquivalenceXOF
                );
    }
}

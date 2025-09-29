package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.ExpenseResponse;
import com.mohdiop.deme_api.dto.response.ProofResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "expenses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "sponsorship_id")
    private Sponsorship sponsorship;

    @ManyToOne
    @JoinColumn(nullable = false, name = "need_id")
    private Need needToSatisfy;

    @Column(nullable = false)
    private String expenseName;

    private String expenseDescription;

    @Column(nullable = false)
    private Double expenseAmount;

    @Column(nullable = false)
    private LocalDateTime expenseMadeAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proof_id", nullable = false)
    private Proof proof;

    public ExpenseResponse toResponse() {
        return new ExpenseResponse(
                expenseId,
                sponsorship.getSponsorshipId(),
                needToSatisfy.getNeedId(),
                expenseName,
                expenseDescription,
                expenseAmount,
                expenseMadeAt,
                new ProofResponse(
                        proof.getProofId(),
                        proof.getProofFileType(),
                        proof.getProofType(),
                        proof.getProofUrl(),
                        proof.getProofUploadedAt()
                )
        );
    }
}

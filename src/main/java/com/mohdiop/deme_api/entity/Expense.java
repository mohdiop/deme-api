package com.mohdiop.deme_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "expenses")
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
    private LocalDateTime expenseMadeAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proof_id", nullable = false)
    private Proof proof;
}

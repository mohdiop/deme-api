package com.mohdiop.deme_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "arguments")
public class Argument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long argumentId;

    @Column(nullable = false)
    private String argumentBody;

    @Column(nullable = false)
    private LocalDateTime argumentDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proof_id")
    private Proof proof;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "report_id")
    private Report relatedReport;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "author_id")
    private User author;
}

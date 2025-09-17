package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.NeedType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "needs")
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
    private Boolean needSatisfied = false;
}

package com.mohdiop.deme_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private String activityName;

    private String activityDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String studentWords;

    @Column(nullable = false)
    private LocalDateTime activityCreatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime activityDoneAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proof_id", nullable = false)
    private Proof proof;
}

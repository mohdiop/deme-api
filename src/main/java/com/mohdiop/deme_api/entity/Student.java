package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "students")
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School studentSchool;

    @Column(nullable = false)
    private String studentFirstName;

    @Column(nullable = false)
    private String studentLastName;

    @Column(nullable = false)
    private LocalDateTime studentBornOn;

    private String studentBornAt;

    @Column(nullable = false)
    private StudentGender studentGender;

    @Column(nullable = false)
    private String studentAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private StudentLevel studentLevel;

    private String studentSpeciality;

    @Column(nullable = false)
    private Double studentFunds = 0D;
}

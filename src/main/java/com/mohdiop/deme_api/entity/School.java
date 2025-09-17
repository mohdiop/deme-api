package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "schools")
public class School extends User {

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private String schoolIdentificationNumber;

    @Column(nullable = false)
    private String schoolAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    @OneToMany(mappedBy = "studentSchool", cascade = CascadeType.ALL)
    private Set<Student> schoolStudents;
}

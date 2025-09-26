package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "students")
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;

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
    private Double studentFunds;

    @OneToMany(cascade = CascadeType.ALL)
    @Transient
    @Size(max = 2)
    private Set<Tutor> tutors;

    @Builder
    public Student(
            Long id,
            String firstName,
            String lastName,
            LocalDateTime bornOn,
            String bornAt,
            StudentGender gender,
            String address,
            StudentLevel level,
            String speciality,
            Double funds,
            String phone,
            String email,
            String password,
            String pictureUrl,
            Set<UserRole> roles,
            UserState state,
            Set<Tutor> tutors
    ) {
        super(id, phone, email, password, pictureUrl, roles, LocalDateTime.now(), state);
        this.studentFirstName = firstName;
        this.studentLastName = lastName;
        this.studentBornOn = bornOn;
        this.studentBornAt = bornAt;
        this.studentGender = gender;
        this.studentAddress = address;
        this.studentLevel = level;
        this.studentSpeciality = speciality;
        this.studentFunds = funds;
        this.tutors = tutors;
    }

    public StudentResponse toResponse() {
        return new StudentResponse(
                getUserId(),
                organization.getUserId(),
                establishment.getEstablishmentId(),
                studentFirstName,
                studentLastName,
                studentBornOn,
                studentBornAt,
                studentGender,
                studentAddress,
                studentLevel,
                studentSpeciality,
                getUserPhone(),
                getUserEmail(),
                getUserRoles(),
                getUserState(),
                getUserCreatedAt()
        );
    }
}

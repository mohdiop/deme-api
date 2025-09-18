package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.entity.enumeration.SchoolType;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private Set<Student> schoolStudents;

    @Builder
    public School(
            Long id,
            String name,
            String identificationNumber,
            String address,
            SchoolType type,
            String phone,
            String email,
            String password,
            Set<UserRole> roles,
            UserState state
    ) {
        super(id, phone, email, password, roles, LocalDateTime.now(), state);
        this.schoolName = name;
        this.schoolIdentificationNumber = identificationNumber;
        this.schoolAddress = address;
        this.schoolType = type;
    }

    public SchoolResponse toResponse() {
        return new SchoolResponse(
                getUserId(),
                schoolName,
                schoolIdentificationNumber,
                schoolAddress,
                schoolType,
                getUserPhone(),
                getUserEmail(),
                getUserRoles(),
                getUserState(),
                getUserCreatedAt()
        );
    }
}

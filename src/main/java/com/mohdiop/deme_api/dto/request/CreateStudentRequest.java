package com.mohdiop.deme_api.dto.request;

import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.entity.enumeration.StudentLevel;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreateStudentRequest(
        Long schoolId,
        String firstName,
        String lastName,
        LocalDateTime bornOn,
        String bornAt,
        StudentGender gender,
        String address,
        StudentLevel level,
        String speciality,
        String phone,
        String email,
        String password,
        Set<UserRole> roles,
        UserState state
) {
    public Student toSchoollessStudent() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .bornOn(bornOn)
                .bornAt(bornAt)
                .gender(gender)
                .address(address)
                .level(level)
                .speciality(speciality)
                .phone(phone)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .roles(new HashSet<>(List.of(UserRole.ROLE_STUDENT)))
                .state(UserState.ACTIVE)
                .build();
    }
}

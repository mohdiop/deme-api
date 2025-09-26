package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.TutorResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tutors")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tutorId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String tutorFirstName;

    @Column(nullable = false)
    private String tutorLastName;

    @Column(nullable = false)
    private String tutorPhone;

    private String tutorEmail;

    @Column(nullable = false)
    private String tutorAddress;

    public TutorResponse toResponse() {
        return new TutorResponse(
                student.getUserId(),
                tutorFirstName,
                tutorLastName,
                tutorPhone,
                tutorEmail,
                tutorAddress
        );
    }
}

package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public StudentService(
            StudentRepository studentRepository,
            SchoolRepository schoolRepository
    ) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest createStudentRequest) {
        var student = createStudentRequest.toSchoollessStudent();
        student.setSchool(
                schoolRepository.findById(createStudentRequest.schoolId())
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "École introuvable."
                                )
                        )
        );
        return studentRepository.save(student).toResponse();
    }
}

package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public StudentService(
            StudentRepository studentRepository,
            SchoolRepository schoolRepository, UserRepository userRepository
    ) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest createStudentRequest) {
        if (createStudentRequest.email() != null && userRepository.findByUserEmail(createStudentRequest.email()).isPresent()) {
            throw new EntityExistsException("Email invalide!");
        }
        if (userRepository.findByUserPhone(createStudentRequest.phone()).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide!");
        }
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

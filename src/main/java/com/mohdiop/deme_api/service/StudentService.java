package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateStudentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentBySchoolRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentByStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public StudentResponse createStudent(
            Long schoolId,
            CreateStudentRequest createStudentRequest
    ) {
        if (createStudentRequest.email() != null && userRepository.findByUserEmail(createStudentRequest.email()).isPresent()) {
            throw new EntityExistsException("Email invalide!");
        }
        if (userRepository.findByUserPhone(createStudentRequest.phone()).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide!");
        }
        var student = createStudentRequest.toSchoollessStudent();
        student.setSchool(
                schoolRepository.findById(schoolId)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "École introuvable."
                                )
                        )
        );
        return studentRepository.save(student).toResponse();
    }

    public StudentResponse updateStudentBySchool(
            Long schoolId,
            Long studentId,
            UpdateStudentBySchoolRequest updateStudentBySchoolRequest
    ) {
        validatePhoneAndEmailOrThrowException(updateStudentBySchoolRequest.phone(), updateStudentBySchoolRequest.email());
        Student studentToUpdate = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        if(!Objects.equals(schoolId, studentToUpdate.getSchool().getUserId())) {
            throw new AccessDeniedException("Cet(te) élève ne fait pas partie de cette école.");
        }
        if (updateStudentBySchoolRequest.firstName() != null) {
            studentToUpdate.setStudentFirstName(updateStudentBySchoolRequest.firstName());
        }
        if (updateStudentBySchoolRequest.lastName() != null) {
            studentToUpdate.setStudentLastName(studentToUpdate.getStudentLastName());
        }
        if (updateStudentBySchoolRequest.bornOn() != null) {
            studentToUpdate.setStudentBornOn(updateStudentBySchoolRequest.bornOn());
        }
        if (updateStudentBySchoolRequest.bornAt() != null) {
            studentToUpdate.setStudentBornAt(updateStudentBySchoolRequest.bornAt());
        }
        if (updateStudentBySchoolRequest.gender() != null) {
            studentToUpdate.setStudentGender(updateStudentBySchoolRequest.gender());
        }
        if (updateStudentBySchoolRequest.address() != null) {
            studentToUpdate.setStudentAddress(updateStudentBySchoolRequest.address());
        }
        if (updateStudentBySchoolRequest.level() != null) {
            studentToUpdate.setStudentLevel(updateStudentBySchoolRequest.level());
        }
        if (updateStudentBySchoolRequest.speciality() != null) {
            studentToUpdate.setStudentSpeciality(updateStudentBySchoolRequest.speciality());
        }
        if (updateStudentBySchoolRequest.email() != null) {
            studentToUpdate.setUserEmail(updateStudentBySchoolRequest.email());
        }
        if (updateStudentBySchoolRequest.phone() != null) {
            studentToUpdate.setUserPhone(updateStudentBySchoolRequest.phone());
        }
        if (updateStudentBySchoolRequest.password() != null) {
            studentToUpdate.setUserPassword(
                    BCrypt.hashpw(updateStudentBySchoolRequest.password(), BCrypt.gensalt())
            );
        }
        return studentRepository.save(studentToUpdate).toResponse();
    }

    public StudentResponse updateByStudent(
            Long studentId,
            UpdateStudentByStudentRequest updateStudentByStudentRequest
    ) {
        validatePhoneAndEmailOrThrowException(updateStudentByStudentRequest.phone(), updateStudentByStudentRequest.email());
        Student studentToUpdate = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        if (updateStudentByStudentRequest.email() != null) {
            studentToUpdate.setUserEmail(updateStudentByStudentRequest.email());
        }
        if (updateStudentByStudentRequest.phone() != null) {
            studentToUpdate.setUserPhone(updateStudentByStudentRequest.phone());
        }
        if (updateStudentByStudentRequest.password() != null) {
            studentToUpdate.setUserPassword(
                    BCrypt.hashpw(updateStudentByStudentRequest.password(), BCrypt.gensalt())
            );
        }
        return studentRepository.save(studentToUpdate).toResponse();
    }

    public List<StudentResponse> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) return new ArrayList<>();
        return allStudents.stream().map(Student::toResponse).toList();
    }

    public void validatePhoneAndEmailOrThrowException(String phone, String email) {
        if (email != null && userRepository.findByUserEmail(email).isPresent()) {
            throw new EntityExistsException("Email invalide.");
        }
        if (userRepository.findByUserPhone(phone).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide.");
        }
    }
}

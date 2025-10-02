package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateStudentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentBySchoolRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentByStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.repository.*;
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
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final SponsorshipRepository sponsorshipRepository;

    public StudentService(StudentRepository studentRepository, OrganizationRepository organizationRepository, UserRepository userRepository, EstablishmentRepository establishmentRepository, SponsorshipRepository sponsorshipRepository) {
        this.studentRepository = studentRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
        this.sponsorshipRepository = sponsorshipRepository;
    }

    public StudentResponse createStudent(Long organizationId, CreateStudentRequest createStudentRequest) {
        validatePhoneAndEmailOrThrowException(createStudentRequest.phone(), createStudentRequest.email());
        var student = createStudentRequest.toOrganizationLessAndEstablishmentLessStudent();
        student.setOrganization(organizationRepository.findById(organizationId).orElseThrow(() -> new EntityNotFoundException("Organisation introuvable.")));
        student.setEstablishment(establishmentRepository.findById(createStudentRequest.establishmentId()).orElseThrow(() -> new EntityNotFoundException("École introuvable.")));
        return studentRepository.save(student).toResponse();
    }

    public StudentResponse updateStudentByOrganization(Long organizationId, Long studentId, UpdateStudentBySchoolRequest updateStudentBySchoolRequest) {
        validatePhoneAndEmailOrThrowException(updateStudentBySchoolRequest.phone(), updateStudentBySchoolRequest.email());
        Student studentToUpdate = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Elève introuvable."));
        if (!Objects.equals(organizationId, studentToUpdate.getOrganization().getUserId())) {
            throw new AccessDeniedException("Cet(te) élève n'est pas affilié(e) à cette organisation.");
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
            studentToUpdate.setUserPassword(BCrypt.hashpw(updateStudentBySchoolRequest.password(), BCrypt.gensalt()));
        }
        return studentRepository.save(studentToUpdate).toResponse();
    }

    public StudentResponse updateByStudent(Long studentId, UpdateStudentByStudentRequest updateStudentByStudentRequest) {
        validatePhoneAndEmailOrThrowException(updateStudentByStudentRequest.phone(), updateStudentByStudentRequest.email());
        Student studentToUpdate = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Elève introuvable."));
        if (updateStudentByStudentRequest.email() != null) {
            studentToUpdate.setUserEmail(updateStudentByStudentRequest.email());
        }
        if (updateStudentByStudentRequest.phone() != null) {
            studentToUpdate.setUserPhone(updateStudentByStudentRequest.phone());
        }
        if (updateStudentByStudentRequest.password() != null) {
            studentToUpdate.setUserPassword(BCrypt.hashpw(updateStudentByStudentRequest.password(), BCrypt.gensalt()));
        }
        return studentRepository.save(studentToUpdate).toResponse();
    }

    public List<StudentResponse> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) return new ArrayList<>();
        return allStudents.stream().map(Student::toResponse).toList();
    }

    public List<StudentResponse> getStudentsByOrgId(Long orgId) {
        List<Student> allStudents = studentRepository.findByOrganizationUserId(orgId);
        if (allStudents.isEmpty()) return new ArrayList<>();
        return allStudents.stream().map(Student::toResponse).toList();
    }

    public List<StudentResponse> getNonSponsoredStudents() {
        var allStudents = studentRepository.findAll();
        List<StudentResponse> studentResponses = new ArrayList<>();
        for (var student : allStudents) {
            if (sponsorshipRepository.findByStudentUserIdAndSponsorshipState(student.getUserId(), SponsorshipState.FINISHED).isPresent() || sponsorshipRepository.findByStudentUserIdAndSponsorshipState(student.getUserId(), SponsorshipState.STOPPED).isPresent()) {
                studentResponses.add(student.toResponse());
            }
        }
        return studentResponses;
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

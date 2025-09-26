package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateTutorRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateTutorRequest;
import com.mohdiop.deme_api.dto.response.TutorResponse;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.Tutor;
import com.mohdiop.deme_api.repository.StudentRepository;
import com.mohdiop.deme_api.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    public TutorService(TutorRepository tutorRepository, StudentRepository studentRepository) {
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
    }

    public TutorResponse createTutor(
            Long studentId,
            CreateTutorRequest createTutorRequest
    ) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        Tutor tutor = createTutorRequest.toStudentlessTutor();
        tutor.setStudent(student);
        return tutorRepository.save(tutor).toResponse();
    }

    public TutorResponse updateTutor(
            Long tutorId,
            UpdateTutorRequest updateTutorRequest
    ) {
        Tutor tutorToUpdate = tutorRepository.findById(tutorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Tuteur introuvable.")
                );
        if (updateTutorRequest.firstName() != null) {
            tutorToUpdate.setTutorFirstName(updateTutorRequest.firstName());
        }
        if (updateTutorRequest.lastName() != null) {
            tutorToUpdate.setTutorLastName(updateTutorRequest.lastName());
        }
        if (updateTutorRequest.phoneNumber() != null) {
            tutorToUpdate.setTutorPhone(updateTutorRequest.phoneNumber());
        }
        if (updateTutorRequest.email() != null) {
            tutorToUpdate.setTutorEmail(updateTutorRequest.email());
        }
        if (updateTutorRequest.address() != null) {
            tutorToUpdate.setTutorAddress(updateTutorRequest.address());
        }
        return tutorRepository.save(tutorToUpdate).toResponse();
    }

    public List<TutorResponse> getStudentTutors(
            Long studentId
    ) {
        List<Tutor> tutors = tutorRepository.findByStudentUserId(studentId);
        if (tutors.isEmpty()) return new ArrayList<>();
        return tutors.stream().map(Tutor::toResponse).toList();
    }

    public List<TutorResponse> getAllTutors() {
        List<Tutor> tutors = tutorRepository.findAll();
        if (tutors.isEmpty()) return new ArrayList<>();
        return tutors.stream().map(Tutor::toResponse).toList();
    }
}

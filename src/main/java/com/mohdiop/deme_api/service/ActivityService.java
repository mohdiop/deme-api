package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateActivityRequest;
import com.mohdiop.deme_api.dto.response.ActivityResponse;
import com.mohdiop.deme_api.entity.Activity;
import com.mohdiop.deme_api.entity.Proof;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import com.mohdiop.deme_api.repository.ActivityRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final StudentRepository studentRepository;

    private final ProofService proofService;

    public ActivityService(ActivityRepository activityRepository, StudentRepository studentRepository, ProofService proofService) {
        this.activityRepository = activityRepository;
        this.studentRepository = studentRepository;
        this.proofService = proofService;
    }

    public ActivityResponse createActivity(
            Long demanderId,
            Long studentId,
            CreateActivityRequest createActivityRequest
    ) {
        Student student = studentRepository.findById(
                studentId
        ).orElseThrow(
                () -> new EntityNotFoundException("Elève introuvable.")
        );
        if (!Objects.equals(demanderId, student.getOrganization().getUserId())) {
            throw new AccessDeniedException("Accès réfusé.");
        }
        Proof proof = proofService.uploadProof(
                createActivityRequest.proof(),
                ProofType.ACTIVITY
        );
        Activity activity = createActivityRequest.toActivity();
        activity.setStudent(student);
        activity.setProof(proof);
        return activityRepository.save(activity).toResponse();
    }

    public List<ActivityResponse> getAllActivities() {
        List<Activity> allActivities = activityRepository.findAll();
        if (allActivities.isEmpty()) return new ArrayList<>();
        return allActivities.stream().map(Activity::toResponse).toList();
    }

    public List<ActivityResponse> getStudentActivities(
            Long studentId
    ) {
        List<Activity> allActivities = activityRepository.findByStudentUserId(studentId);
        if (allActivities.isEmpty()) return new ArrayList<>();
        return allActivities.stream().map(Activity::toResponse).toList();
    }

}

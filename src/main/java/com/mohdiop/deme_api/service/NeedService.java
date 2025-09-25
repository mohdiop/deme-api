package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateNeedRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateNeedRequest;
import com.mohdiop.deme_api.dto.response.NeedResponse;
import com.mohdiop.deme_api.entity.Need;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.repository.NeedRepository;
import com.mohdiop.deme_api.repository.OrganizationRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NeedService {

    private final NeedRepository needRepository;
    private final OrganizationRepository organizationRepository;
    private final StudentRepository studentRepository;

    public NeedService(NeedRepository needRepository, OrganizationRepository organizationRepository, StudentRepository studentRepository) {
        this.needRepository = needRepository;
        this.organizationRepository = organizationRepository;
        this.studentRepository = studentRepository;
    }

    public NeedResponse createNeed(Long demanderId, Long studentId, CreateNeedRequest createNeedRequest) {
        organizationRepository.findById(demanderId)
                .orElseThrow(() -> new EntityNotFoundException("Organisation introuvable."));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Elève introuvable."));
        if (!demanderId.equals(student.getOrganization().getUserId())) {
            throw new AccessDeniedException("Cet(te) élève n'est pas affilié(e) à cette organisation.");
        }
        Need needToStore = createNeedRequest.toStudentlessNeed();
        needToStore.setStudent(student);
        return needRepository.save(needToStore).toResponse();
    }

    public NeedResponse updateNeed(
            Long needId,
            UpdateNeedRequest updateNeedRequest
    ) {
        Need needToUpdate = needRepository.findById(needId)
                .orElseThrow(() -> new EntityNotFoundException("Besoin introuvable."));

        if (updateNeedRequest.description() != null) {
            needToUpdate.setNeedDescription(updateNeedRequest.description());
        }
        if (updateNeedRequest.amount() != null) {
            needToUpdate.setNeededAmount(updateNeedRequest.amount());
        }
        if (updateNeedRequest.type() != null) {
            needToUpdate.setNeedType(updateNeedRequest.type());
        }
        if (updateNeedRequest.emergency() != null) {
            needToUpdate.setNeedEmergency(updateNeedRequest.emergency());
        }
        if (updateNeedRequest.expiresAt() != null) {
            needToUpdate.setNeedExpiresAt(updateNeedRequest.expiresAt());
        }

        return needRepository.save(needToUpdate).toResponse();
    }


    public List<NeedResponse> getAllNeeds() {
        List<Need> allNeeds = needRepository.findAll();
        if (allNeeds.isEmpty()) return new ArrayList<>();
        return allNeeds.stream().map(Need::toResponse).toList();
    }
}

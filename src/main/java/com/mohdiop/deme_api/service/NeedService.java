package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateNeedRequest;
import com.mohdiop.deme_api.dto.request.creation.SendNotificationRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateNeedRequest;
import com.mohdiop.deme_api.dto.response.NeedResponse;
import com.mohdiop.deme_api.entity.Need;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.repository.NeedRepository;
import com.mohdiop.deme_api.repository.OrganizationRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
public class NeedService {

    private final NeedRepository needRepository;
    private final OrganizationRepository organizationRepository;
    private final StudentRepository studentRepository;
    private final SponsorshipRepository sponsorshipRepository;

    private final NotificationService notificationService;

    public NeedService(NeedRepository needRepository, OrganizationRepository organizationRepository, StudentRepository studentRepository, SponsorshipRepository sponsorshipRepository, NotificationService notificationService) {
        this.needRepository = needRepository;
        this.organizationRepository = organizationRepository;
        this.studentRepository = studentRepository;
        this.sponsorshipRepository = sponsorshipRepository;
        this.notificationService = notificationService;
    }

    @Transactional
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
        sendNotificationToSponsorIfExist(studentId, needToStore.getNeedExpiresAt());
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

    public List<NeedResponse> getStudentNeeds(
            Long studentId
    ) {
        List<Need> allNeeds = needRepository.findByStudentUserId(studentId);
        if (allNeeds.isEmpty()) return new ArrayList<>();
        return allNeeds.stream().map(Need::toResponse).toList();
    }

    public void sendNotificationToSponsorIfExist(
            Long studentId,
            LocalDateTime needExpiresAt
    ) {
        Optional<Sponsorship> sp = sponsorshipRepository.findByStudentUserId(studentId);
        if (sp.isEmpty()) return;
        Sponsorship sponsorship = sp.get();
        if (sponsorship.getSponsorshipState() == SponsorshipState.STOPPED
                || sponsorship.getSponsorshipState() == SponsorshipState.FINISHED) return;
        String studentFirstName = sponsorship.getStudent().getStudentFirstName();
        String studentLastName = sponsorship.getStudent().getStudentLastName();
        notificationService.sendNotification(
                new SendNotificationRequest(
                        sponsorship.getSponsor().getUserId(),
                        "Nouveau besoin.",
                        String.format(
                                "Un nouveau besoin a été ajouté pour %s %s et doit expiré le %s.",
                                studentFirstName,
                                studentLastName,
                                needExpiresAt.format(
                                        DateTimeFormatter
                                                .ofPattern(
                                                        "EEEE d MMMM yyyy 'à' HH:mm",
                                                        Locale.FRENCH
                                                )
                                )
                        )
                )
        );
    }
}

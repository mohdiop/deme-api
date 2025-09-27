package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateSponsorshipRequest;
import com.mohdiop.deme_api.dto.request.creation.SendNotificationRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSponsorshipRequest;
import com.mohdiop.deme_api.dto.response.SponsorshipResponse;
import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.repository.SponsorRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepository;
    private final SponsorRepository sponsorRepository;
    private final StudentRepository studentRepository;

    private final NotificationService notificationService;

    DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);

    public SponsorshipService(SponsorshipRepository sponsorshipRepository, SponsorRepository sponsorRepository, StudentRepository studentRepository, NotificationService notificationService) {
        this.sponsorshipRepository = sponsorshipRepository;
        this.sponsorRepository = sponsorRepository;
        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
    }

    public SponsorshipResponse createSponsorship(
            Long sponsorId,
            CreateSponsorshipRequest createSponsorshipRequest
    ) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrain introuvable.")
                );
        Student student = studentRepository.findById(createSponsorshipRequest.studentId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        if (sponsorshipRepository.findByStudentUserIdAndSponsorshipState(
                createSponsorshipRequest.studentId(),
                SponsorshipState.IN_PROGRESS).isPresent()) {
            throw new EntityExistsException(String.format(
                    "Elève déjà %s.", getSponsoredText(student.getStudentGender())
            ));
        }
        Sponsorship sponsorship = createSponsorshipRequest.toStudentlessAndSponsorlessSponsorship();
        sponsorship.setSponsor(sponsor);
        sponsorship.setStudent(student);
        sendSponsorshipStartedNotifToOrg(sponsorship);
        sendSponsorshipStartedNotifToStudent(sponsorship);
        return sponsorshipRepository.save(sponsorship).toResponse();
    }

    public SponsorshipResponse updateSponsorship(
            Long sponsorshipId,
            UpdateSponsorshipRequest updateSponsorshipRequest
    ) {
        Sponsorship sponsorshipToUpdate = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        if (updateSponsorshipRequest.endAt() != null) {
            sponsorshipToUpdate.setSponsorshipEndAt(updateSponsorshipRequest.endAt());
        }
        if (updateSponsorshipRequest.type() != null) {
            sponsorshipToUpdate.setSponsorshipType(updateSponsorshipRequest.type());
        }
        return sponsorshipRepository.save(sponsorshipToUpdate).toResponse();
    }

    public SponsorshipResponse extendSponsorship(
            Long sponsorshipId,
            LocalDateTime newEndDate
    ) throws BadRequestException {
        Sponsorship sponsorshipToExtend = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        if (sponsorshipToExtend.getSponsorshipState() != SponsorshipState.FINISHED) {
            throw new UnsupportedOperationException("Impossible de prolonger un parrainage non terminé.");
        }
        if (sponsorshipToExtend.getSponsorshipEndAt().isAfter(newEndDate)) {
            throw new BadRequestException("La date de prolongation doit être dans le futur.");
        }
        sponsorshipToExtend.setSponsorshipEndAt(newEndDate);
        sponsorshipToExtend.setSponsorshipState(SponsorshipState.IN_PROGRESS);
        sendSponsorshipExtendedNotifToOrg(sponsorshipToExtend.getStudent(), newEndDate);
        sendSponsorshipExtendedNotifToStudent(sponsorshipToExtend.getStudent().getUserId(), newEndDate);
        return sponsorshipRepository.save(sponsorshipToExtend).toResponse();
    }

    public List<SponsorshipResponse> getSponsorshipsBySponsorId(
            Long sponsorId
    ) {
        List<Sponsorship> sponsorships = sponsorshipRepository.findBySponsorUserId(sponsorId);
        if (sponsorships.isEmpty()) return new ArrayList<>();
        return sponsorships.stream().map(Sponsorship::toResponse).toList();
    }

    public SponsorshipResponse getSponsorshipByStudentId(
            Long studentId
    ) {
        return sponsorshipRepository.findByStudentUserId(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cet(te) élève n'est pas parrainé(e).")
                ).toResponse();
    }

    public List<SponsorshipResponse> getAllSponsorships() {
        List<Sponsorship> sponsorships = sponsorshipRepository.findAll();
        if (sponsorships.isEmpty()) return new ArrayList<>();
        return sponsorships.stream().map(Sponsorship::toResponse).toList();
    }

    public void sendSponsorshipExtendedNotifToOrg(
            Student student,
            LocalDateTime endAt
    ) {
        String title = "Parrainage prolongé.";
        String content = String.format(
                "Le parrainage de votre élève %s %s est prolongé jusqu'au %s",
                student.getStudentFirstName(),
                student.getStudentLastName(),
                endAt.format(formatter)
        );
        notificationService.sendNotification(
                new SendNotificationRequest(
                        student.getOrganization().getUserId(),
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipExtendedNotifToStudent(
            Long studentId,
            LocalDateTime endAt
    ) {
        String title = "Parrainage prolongé";
        String content = String.format(
                "Votre parrainage a été prolongé jusqu'au %s.",
                endAt.format(formatter)
        );
        notificationService.sendNotification(
                new SendNotificationRequest(
                        studentId,
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipStartedNotifToOrg(
            Sponsorship sponsorship
    ) {
        String sponsoredText = getSponsoredText(sponsorship.getStudent().getStudentGender());
        String title = String.format("Votre élève est %s.", sponsoredText);
        String content = "";
        switch (sponsorship.getSponsorshipType()) {
            case ANONYMOUS -> content = String.format(
                    "Votre élève %s %s a reçu un parrainage anonyme du %s au %s.",
                    sponsorship.getStudent().getStudentFirstName(),
                    sponsorship.getStudent().getStudentLastName(),
                    sponsorship.getSponsorshipStartedAt().format(formatter),
                    sponsorship.getSponsorshipEndAt().format(formatter)
            );
            case IDENTIFIED -> content = String.format(
                    "Votre élève %s %s est maintenant %s par %s %s du %s au %s.",
                    sponsorship.getStudent().getStudentFirstName(),
                    sponsorship.getStudent().getStudentLastName(),
                    sponsoredText,
                    sponsorship.getSponsor().getSponsorFirstName(),
                    sponsorship.getSponsor().getSponsorLastName(),
                    sponsorship.getSponsorshipStartedAt().format(formatter),
                    sponsorship.getSponsorshipEndAt().format(formatter)
            );
        }
        notificationService.sendNotification(
                new SendNotificationRequest(
                        sponsorship.getStudent().getOrganization().getUserId(),
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipStartedNotifToStudent(
            Sponsorship sponsorship
    ) {
        String title = "Nouveau parrainage.";
        String content = "";
        switch (sponsorship.getSponsorshipType()) {
            case ANONYMOUS -> content = String.format(
                    "Vous êtes maintenant %s.",
                    getSponsoredText(
                            sponsorship.getStudent().getStudentGender()
                    ));
            case IDENTIFIED -> content = String.format(
                    "Vous êtes maintenant %s par %s %s.",
                    getSponsoredText(
                            sponsorship.getStudent().getStudentGender()
                    ),
                    sponsorship.getSponsor().getSponsorFirstName(),
                    sponsorship.getSponsor().getSponsorLastName()
            );
        }
        notificationService.sendNotification(
                new SendNotificationRequest(
                        sponsorship.getStudent().getUserId(),
                        title,
                        content
                )
        );
    }

    public String getSponsoredText(
            StudentGender gender
    ) {
        return (gender == StudentGender.FEMALE)
                ? "parrainée"
                : "parrainé";
    }

}

package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateSponsorshipRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSponsorshipRequest;
import com.mohdiop.deme_api.dto.response.SponsorshipResponse;
import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.repository.SponsorRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
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

    @Transactional
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
        Optional<Sponsorship> potentialSponsorship = sponsorshipRepository.findByStudentUserId(
                createSponsorshipRequest.studentId()
        );
        if (potentialSponsorship.isPresent()) {
            Sponsorship sponsorship = potentialSponsorship.get();
            if (
                    sponsorship.getSponsorshipState() == SponsorshipState.IN_PROGRESS
                            || sponsorship.getSponsorshipState() == SponsorshipState.FROM_TRANSFER
                            || sponsorship.getSponsorshipState() == SponsorshipState.PENDING
            ) {
                throw new EntityExistsException("Elève indisponible pour parrainage.");
            }
        }
        Sponsorship sponsorship = createSponsorshipRequest.toStudentlessAndSponsorlessSponsorship();
        sponsorship.setSponsor(sponsor);
        sponsorship.setStudent(student);
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

    @Transactional
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
        notificationService.sendSponsorshipExtendedNotifToOrg(sponsorshipToExtend.getStudent(), newEndDate);
        notificationService.sendSponsorshipExtendedNotifToStudent(sponsorshipToExtend.getStudent().getUserId(), newEndDate);
        return sponsorshipRepository.save(sponsorshipToExtend).toResponse();
    }

    public SponsorshipResponse changeStudentInfoAccessibleState(
            Long sponsorshipId,
            Long studentId
    ) {
        Sponsorship sponsorship = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        if (!sponsorship.getStudent().getUserId().equals(studentId)) {
            throw new AccessDeniedException("Accès non autorisé.");
        }
        sponsorship.setStudentInfoAccessible(!sponsorship.getStudentInfoAccessible());
        return sponsorshipRepository.save(sponsorship).toResponse();
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

}

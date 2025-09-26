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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepository;
    private final SponsorRepository sponsorRepository;
    private final StudentRepository studentRepository;

    public SponsorshipService(SponsorshipRepository sponsorshipRepository, SponsorRepository sponsorRepository, StudentRepository studentRepository) {
        this.sponsorshipRepository = sponsorshipRepository;
        this.sponsorRepository = sponsorRepository;
        this.studentRepository = studentRepository;
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
            throw new EntityExistsException("Elève déjà parrainé.");
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

    public SponsorshipResponse extendSponsorship(
            Long sponsorshipId,
            LocalDateTime newEndDate
    ) throws BadRequestException {
        Sponsorship sponsorshipToUpdate = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        if (sponsorshipToUpdate.getSponsorshipState() != SponsorshipState.FINISHED) {
            throw new UnsupportedOperationException("Impossible de prolonger un parrainage non terminé.");
        }
        if (sponsorshipToUpdate.getSponsorshipEndAt().isAfter(newEndDate)) {
            throw new BadRequestException("La date de prolongation doit être dans le futur.");
        }
        sponsorshipToUpdate.setSponsorshipEndAt(newEndDate);
        sponsorshipToUpdate.setSponsorshipState(SponsorshipState.IN_PROGRESS);
        return sponsorshipRepository.save(sponsorshipToUpdate).toResponse();
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

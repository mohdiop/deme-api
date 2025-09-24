package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateSponsorRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSponsorRequest;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.enumeration.SponsorType;
import com.mohdiop.deme_api.repository.SponsorRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final UserRepository userRepository;

    public SponsorService(SponsorRepository sponsorRepository, UserRepository userRepository) {
        this.sponsorRepository = sponsorRepository;
        this.userRepository = userRepository;
    }

    public SponsorResponse createSponsor(CreateSponsorRequest createSponsorRequest) {
        validatePhoneAndEmailOrThrowException(createSponsorRequest.phone(), createSponsorRequest.email());
        return sponsorRepository.save(
                createSponsorRequest.toSponsor()
        ).toResponse();
    }

    public SponsorResponse updateSponsor(Long userId, UpdateSponsorRequest updateSponsorRequest) {
        Sponsor sponsorToUpdate = sponsorRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable."));
        validatePhoneAndEmailOrThrowException(updateSponsorRequest.phone(), updateSponsorRequest.email());
        if (updateSponsorRequest.firstName() != null) {
            sponsorToUpdate.setSponsorFirstName(updateSponsorRequest.firstName());
        }
        if (updateSponsorRequest.lastName() != null) {
            sponsorToUpdate.setSponsorLastName(updateSponsorRequest.lastName());
        }
        if (updateSponsorRequest.sponsorType() != null) {
            if (updateSponsorRequest.sponsorType() == SponsorType.INDIVIDUAL) {
                sponsorToUpdate.setSponsorOrganizationName(null);
            }
            sponsorToUpdate.setSponsorType(updateSponsorRequest.sponsorType());
        }
        if (updateSponsorRequest.organizationName() != null && sponsorToUpdate.getSponsorType() == SponsorType.ORGANIZATIONAL) {
            sponsorToUpdate.setSponsorOrganizationName(updateSponsorRequest.organizationName());
        }
        if (updateSponsorRequest.address() != null) {
            sponsorToUpdate.setSponsorAddress(updateSponsorRequest.address());
        }
        if (updateSponsorRequest.email() != null) {
            sponsorToUpdate.setUserEmail(updateSponsorRequest.email());
        }
        if (updateSponsorRequest.phone() != null) {
            sponsorToUpdate.setUserPhone(updateSponsorRequest.phone());
        }
        if (updateSponsorRequest.password() != null) {
            sponsorToUpdate.setUserPassword(
                    BCrypt.hashpw(updateSponsorRequest.password(), BCrypt.gensalt())
            );
        }
        return sponsorRepository.save(sponsorToUpdate)
                .toResponse();
    }

    public List<SponsorResponse> getAllSponsors() {
        List<Sponsor> allSponsors = sponsorRepository.findAll();
        if (allSponsors.isEmpty()) return new ArrayList<>();
        return allSponsors.stream().map(Sponsor::toResponse).toList();
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

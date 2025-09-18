package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateSponsorRequest;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.repository.SponsorRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final UserRepository userRepository;

    public SponsorService(SponsorRepository sponsorRepository, UserRepository userRepository) {
        this.sponsorRepository = sponsorRepository;
        this.userRepository = userRepository;
    }

    public SponsorResponse createSponsor(CreateSponsorRequest createSponsorRequest) {
        if(createSponsorRequest.email() != null && userRepository.findByUserEmail(createSponsorRequest.email()).isPresent()) {
            throw new EntityExistsException("Email invalide!");
        }
        if(userRepository.findByUserPhone(createSponsorRequest.phone()).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide!");
        }
        return sponsorRepository.save(
                createSponsorRequest.toSponsor()
        ).toResponse();
    }
}

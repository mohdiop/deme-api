package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateSponsorRequest;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.repository.SponsorRepository;
import org.springframework.stereotype.Service;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;

    public SponsorService(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    public SponsorResponse createSponsor(CreateSponsorRequest createSponsorRequest) {
        return sponsorRepository.save(
                createSponsorRequest.toSponsor()
        ).toResponse();
    }
}

package com.mohdiop.deme_api.job;

import com.mohdiop.deme_api.repository.SponsorshipRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SponsorshipEndJob {

    private final SponsorshipRepository sponsorshipRepository;

    public SponsorshipEndJob(SponsorshipRepository sponsorshipRepository) {
        this.sponsorshipRepository = sponsorshipRepository;
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void endSponsorships() {
        sponsorshipRepository.endSponsorships();
    }
}

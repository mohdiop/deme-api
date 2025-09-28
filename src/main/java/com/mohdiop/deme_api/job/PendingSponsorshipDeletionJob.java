package com.mohdiop.deme_api.job;

import com.mohdiop.deme_api.repository.SponsorshipRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PendingSponsorshipDeletionJob {

    private final SponsorshipRepository sponsorshipRepository;

    public PendingSponsorshipDeletionJob(SponsorshipRepository sponsorshipRepository) {
        this.sponsorshipRepository = sponsorshipRepository;
    }

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void deletePendingSponsorships() {
        sponsorshipRepository.deletePendingSponsorships();
    }
}

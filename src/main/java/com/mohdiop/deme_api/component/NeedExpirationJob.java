package com.mohdiop.deme_api.component;

import com.mohdiop.deme_api.repository.NeedRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NeedExpirationJob {

    private final NeedRepository needRepository;

    public NeedExpirationJob(NeedRepository needRepository) {
        this.needRepository = needRepository;
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void expireNeeds() {
        needRepository.expireNeeds();
    }

}

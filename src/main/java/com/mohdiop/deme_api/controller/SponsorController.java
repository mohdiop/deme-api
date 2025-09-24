package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.update.UpdateSponsorRequest;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.SponsorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sponsors")
public class SponsorController {

    private final SponsorService sponsorService;
    private final AuthenticationService authenticationService;

    public SponsorController(SponsorService sponsorService, AuthenticationService authenticationService) {
        this.sponsorService = sponsorService;
        this.authenticationService = authenticationService;
    }

    @PatchMapping
    public ResponseEntity<SponsorResponse> updateSponsor(
            @Valid @RequestBody UpdateSponsorRequest updateSponsorRequest
    ) {
        return ResponseEntity.ok(
                sponsorService.updateSponsor(
                        authenticationService.getCurrentUserId(),
                        updateSponsorRequest
                )
        );
    }
}

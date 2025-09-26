package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateSponsorshipRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSponsorshipRequest;
import com.mohdiop.deme_api.dto.response.SponsorshipResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.SponsorshipService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sponsorships")
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;
    private final AuthenticationService authenticationService;

    public SponsorshipController(SponsorshipService sponsorshipService, AuthenticationService authenticationService) {
        this.sponsorshipService = sponsorshipService;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @PostMapping
    public ResponseEntity<SponsorshipResponse> createSponsorship(
            @Valid @RequestBody CreateSponsorshipRequest createSponsorshipRequest
    ) {
        return new ResponseEntity<>(
                sponsorshipService.createSponsorship(createSponsorshipRequest),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @PatchMapping("/{sponsorshipId}")
    public ResponseEntity<SponsorshipResponse> updateSponsorship(
            @PathVariable Long sponsorshipId,
            @Valid @RequestBody UpdateSponsorshipRequest updateSponsorshipRequest
    ) {
        return ResponseEntity.ok(
                sponsorshipService.updateSponsorship(
                        sponsorshipId,
                        updateSponsorshipRequest
                )
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @PostMapping("/{sponsorshipId}")
    public ResponseEntity<SponsorshipResponse> extendSponsorship(
            @PathVariable Long sponsorshipId,
            @NotNull(message = "La date de fin est obligatoire.")
            @Future(message = "La date de fin doit être dans le futur.")
            @RequestBody LocalDateTime newEndDate
    ) throws BadRequestException {
        return ResponseEntity.ok(
                sponsorshipService.extendSponsorship(
                        sponsorshipId,
                        newEndDate
                )
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @GetMapping
    public ResponseEntity<List<SponsorshipResponse>> mySponsorships() {
        return ResponseEntity.ok(
                sponsorshipService.getSponsorshipsBySponsorId(
                        authenticationService.getCurrentUserId()
                )
        );
    }
}

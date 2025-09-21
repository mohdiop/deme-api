package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.AuthenticationRequest;
import com.mohdiop.deme_api.dto.request.CreateSponsorRequest;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.SponsorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final SponsorService sponsorService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(SponsorService sponsorService, AuthenticationService authenticationService) {
        this.sponsorService = sponsorService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register/sponsor")
    public ResponseEntity<SponsorResponse> createSponsor(
            @Valid @RequestBody CreateSponsorRequest createSponsorRequest
    ) {
        return new ResponseEntity<>(
                sponsorService.createSponsor(createSponsorRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationService.TokenPairResponse> login(
            @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(
                authenticationService.authenticate(authenticationRequest)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationService.TokenPairResponse> refreshToken(
            @RequestBody String refreshToken
    ) {
        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
    }
}

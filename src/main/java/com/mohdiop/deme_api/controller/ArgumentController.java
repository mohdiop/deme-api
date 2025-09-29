package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateArgumentRequest;
import com.mohdiop.deme_api.dto.response.ArgumentResponse;
import com.mohdiop.deme_api.service.ArgumentService;
import com.mohdiop.deme_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sponsorships/expenses/reports")
public class ArgumentController {

    private final ArgumentService argumentService;
    private final AuthenticationService authenticationService;

    public ArgumentController(ArgumentService argumentService, AuthenticationService authenticationService) {
        this.argumentService = argumentService;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{reportId}/arguments")
    public ResponseEntity<ArgumentResponse> createArgument(
            @PathVariable Long reportId,
            @Valid @ModelAttribute CreateArgumentRequest createArgumentRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                argumentService.createArgument(
                        authenticationService.getCurrentUserId(),
                        reportId,
                        createArgumentRequest
                ),
                HttpStatus.CREATED
        );
    }
}

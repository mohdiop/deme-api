package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateEstablishmentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateEstablishmentRequest;
import com.mohdiop.deme_api.dto.response.EstablishmentResponse;
import com.mohdiop.deme_api.service.EstablishmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/establishments")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstablishmentResponse> createEstablishment(
            @Valid @RequestBody CreateEstablishmentRequest createEstablishmentRequest
    ) {
        return new ResponseEntity<>(
                establishmentService.createEstablishment(createEstablishmentRequest),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{establishmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstablishmentResponse> updateEstablishment(
            @PathVariable Long establishmentId,
            @Valid @RequestBody UpdateEstablishmentRequest updateEstablishmentRequest
    ) {
        return ResponseEntity.ok(
                establishmentService.updateEstablishment(
                        establishmentId,
                        updateEstablishmentRequest
                )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<EstablishmentResponse>> getAllEstablishments() {
        return ResponseEntity.ok(
                establishmentService.getAllEstablishments()
        );
    }
}

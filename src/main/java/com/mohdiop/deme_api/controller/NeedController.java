package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateNeedRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateNeedRequest;
import com.mohdiop.deme_api.dto.response.NeedResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.NeedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/students")
public class NeedController {

    private final NeedService needService;
    private final AuthenticationService authenticationService;

    public NeedController(NeedService needService, AuthenticationService authenticationService) {
        this.needService = needService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/{studentId}/needs")
    public ResponseEntity<NeedResponse> createNeed(
            @PathVariable Long studentId,
            @Valid @RequestBody CreateNeedRequest createNeedRequest
    ) {
        return new ResponseEntity<>(
                needService.createNeed(
                        authenticationService.getCurrentUserId(),
                        studentId,
                        createNeedRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/needs/{needId}")
    public ResponseEntity<NeedResponse> updateNeed(
            @PathVariable Long needId,
            @Valid @RequestBody UpdateNeedRequest updateNeedRequest
    ) {
        return ResponseEntity.ok(
                needService.updateNeed(
                        needId,
                        updateNeedRequest
                )
        );
    }

    @PreAuthorize("hasAnyRole('SPONSOR','ORGANIZATION', 'SPONSOR')")
    @GetMapping("/{studentId}/needs")
    public ResponseEntity<List<NeedResponse>> getStudentNeeds(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                needService.getStudentNeeds(studentId)
        );
    }
}

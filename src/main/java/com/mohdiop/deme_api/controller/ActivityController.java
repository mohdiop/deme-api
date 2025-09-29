package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateActivityRequest;
import com.mohdiop.deme_api.dto.response.ActivityResponse;
import com.mohdiop.deme_api.service.ActivityService;
import com.mohdiop.deme_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/students")
public class ActivityController {

    private final ActivityService activityService;
    private final AuthenticationService authenticationService;

    public ActivityController(ActivityService activityService, AuthenticationService authenticationService) {
        this.activityService = activityService;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping("/{studentId}/activities")
    public ResponseEntity<ActivityResponse> createActivity(
            @PathVariable Long studentId,
            @Valid @ModelAttribute CreateActivityRequest createActivityRequest
    ) {
        return new ResponseEntity<>(
                activityService.createActivity(
                        authenticationService.getCurrentUserId(),
                        studentId,
                        createActivityRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAnyRole('ORGANIZATION', 'STUDENT', 'SPONSOR')")
    @GetMapping("/{studentId}/activities")
    public ResponseEntity<List<ActivityResponse>> getStudentActivities(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(activityService.getStudentActivities(studentId));
    }
}

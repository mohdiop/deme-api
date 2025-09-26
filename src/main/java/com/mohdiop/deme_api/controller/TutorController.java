package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateTutorRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateTutorRequest;
import com.mohdiop.deme_api.dto.response.TutorResponse;
import com.mohdiop.deme_api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/students")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping("/{studentId}/tutors")
    public ResponseEntity<TutorResponse> createTutor(
            @PathVariable Long studentId,
            @Valid @RequestBody CreateTutorRequest createTutorRequest
    ) {
        return new ResponseEntity<>(
                tutorService.createTutor(
                        studentId,
                        createTutorRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/tutors/{tutorId}")
    public ResponseEntity<TutorResponse> updateTutor(
            @PathVariable Long tutorId,
            @Valid @RequestBody UpdateTutorRequest updateTutorRequest
    ) {
        return ResponseEntity.ok(
                tutorService.updateTutor(
                        tutorId,
                        updateTutorRequest
                )
        );
    }

    @GetMapping("/{studentId}/tutors")
    public ResponseEntity<List<TutorResponse>> getStudentTutors(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                tutorService.getStudentTutors(studentId)
        );
    }
}

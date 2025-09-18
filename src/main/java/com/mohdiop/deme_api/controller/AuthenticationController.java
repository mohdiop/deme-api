package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.AuthenticationRequest;
import com.mohdiop.deme_api.dto.request.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.request.CreateSponsorRequest;
import com.mohdiop.deme_api.dto.request.CreateStudentRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.SchoolService;
import com.mohdiop.deme_api.service.SponsorService;
import com.mohdiop.deme_api.service.StudentService;
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
    private final SchoolService schoolService;
    private final StudentService studentService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(SponsorService sponsorService, SchoolService schoolService, StudentService studentService, AuthenticationService authenticationService) {
        this.sponsorService = sponsorService;
        this.schoolService = schoolService;
        this.studentService = studentService;
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

    @PostMapping("/register/school")
    public ResponseEntity<SchoolResponse> createSchool(
            @Valid @RequestBody CreateSchoolRequest createSchoolRequest
    ) {
        return new ResponseEntity<>(
                schoolService.createSchool(createSchoolRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/register/student")
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest createStudentRequest
    ) {
        return new ResponseEntity<>(
                studentService.createStudent(createStudentRequest),
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

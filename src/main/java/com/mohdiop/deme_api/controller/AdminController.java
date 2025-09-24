package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateOrganizationRequest;
import com.mohdiop.deme_api.dto.response.OrganizationResponse;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final SponsorService sponsorService;
    private final OrganizationService organizationService;
    private final StudentService studentService;
    private final UserService userService;

    public AdminController(SponsorService sponsorService, OrganizationService organizationService, StudentService studentService, UserService userService) {
        this.sponsorService = sponsorService;
        this.organizationService = organizationService;
        this.studentService = studentService;
        this.userService = userService;
    }

    @PostMapping("/register/organizations")
    public ResponseEntity<OrganizationResponse> createSchool(
            @Valid @RequestBody CreateOrganizationRequest createOrganizationRequest
    ) {
        return new ResponseEntity<>(
                organizationService.createOrganization(createOrganizationRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorResponse>> getAllSponsors() {
        return ResponseEntity.ok(sponsorService.getAllSponsors());
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Record>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

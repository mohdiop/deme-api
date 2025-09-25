package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateStudentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateOrganizationRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentBySchoolRequest;
import com.mohdiop.deme_api.dto.response.OrganizationResponse;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.OrganizationService;
import com.mohdiop.deme_api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final StudentService studentService;
    private final AuthenticationService authenticationService;
    private final OrganizationService organizationService;

    public OrganizationController(StudentService studentService, AuthenticationService authenticationService, OrganizationService organizationService) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
        this.organizationService = organizationService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest createStudentRequest
    ) {
        return new ResponseEntity<>(
                studentService.createStudent(
                        authenticationService.getCurrentUserId(),
                        createStudentRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PatchMapping
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @Valid @RequestBody UpdateOrganizationRequest updateOrganizationRequest
    ) {
        return ResponseEntity.ok(
                organizationService.updateOrganization(
                        authenticationService.getCurrentUserId(),
                        updateOrganizationRequest
                )
        );
    }

    @PatchMapping("/students/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long studentId,
            @Valid @RequestBody UpdateStudentBySchoolRequest updateStudentBySchoolRequest
    ) {
        return ResponseEntity.ok(
                studentService.updateStudentBySchool(
                        authenticationService.getCurrentUserId(),
                        studentId,
                        updateStudentBySchoolRequest
                )
        );
    }

}

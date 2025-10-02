package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.update.UpdateStudentByStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    private final AuthenticationService authenticationService;
    private final StudentService studentService;

    public StudentController(AuthenticationService authenticationService, StudentService studentService) {
        this.authenticationService = authenticationService;
        this.studentService = studentService;
    }

    @PatchMapping("/api/v1/students")
    public ResponseEntity<StudentResponse> updateStudent(
            @Valid @RequestBody UpdateStudentByStudentRequest updateStudentByStudentRequest
    ) {
        return ResponseEntity.ok(
                studentService.updateByStudent(
                        authenticationService.getCurrentUserId(),
                        updateStudentByStudentRequest
                )
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @GetMapping("/api/v1/non-sponsored-students")
    public ResponseEntity<List<StudentResponse>> getNonSponsoredStudents() {
        return ResponseEntity.ok(
                studentService.getNonSponsoredStudents()
        );
    }
}

package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.CreateStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schools")
public class SchoolController {

    private final StudentService studentService;
    private final AuthenticationService authenticationService;

    public SchoolController(StudentService studentService, AuthenticationService authenticationService) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
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

}

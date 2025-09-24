package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.update.UpdateStudentByStudentRequest;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final AuthenticationService authenticationService;
    private final StudentService studentService;

    public StudentController(AuthenticationService authenticationService, StudentService studentService) {
        this.authenticationService = authenticationService;
        this.studentService = studentService;
    }

    @PatchMapping
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
}

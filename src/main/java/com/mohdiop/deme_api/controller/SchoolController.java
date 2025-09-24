package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateStudentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSchoolRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateStudentBySchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.SchoolService;
import com.mohdiop.deme_api.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schools")
public class SchoolController {

    private final StudentService studentService;
    private final AuthenticationService authenticationService;
    private final SchoolService schoolService;

    public SchoolController(StudentService studentService, AuthenticationService authenticationService, SchoolService schoolService) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
        this.schoolService = schoolService;
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
    public ResponseEntity<SchoolResponse> updateSchool(
            @Valid @RequestBody UpdateSchoolRequest updateSchoolRequest
    ) {
        return ResponseEntity.ok(
                schoolService.updateSchool(
                        authenticationService.getCurrentUserId(),
                        updateSchoolRequest
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

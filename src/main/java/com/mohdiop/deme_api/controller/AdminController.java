package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.dto.response.SponsorResponse;
import com.mohdiop.deme_api.dto.response.StudentResponse;
import com.mohdiop.deme_api.service.SchoolService;
import com.mohdiop.deme_api.service.SponsorService;
import com.mohdiop.deme_api.service.StudentService;
import com.mohdiop.deme_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final SponsorService sponsorService;
    private final SchoolService schoolService;
    private final StudentService studentService;
    private final UserService userService;

    public AdminController(SponsorService sponsorService, SchoolService schoolService, StudentService studentService, UserService userService) {
        this.sponsorService = sponsorService;
        this.schoolService = schoolService;
        this.studentService = studentService;
        this.userService = userService;
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

    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorResponse>> getAllSponsors() {
        return ResponseEntity.ok(sponsorService.getAllSponsors());
    }

    @GetMapping("/schools")
    public ResponseEntity<List<SchoolResponse>> getAllSchools() {
        return ResponseEntity.ok(schoolService.getAllSchool());
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

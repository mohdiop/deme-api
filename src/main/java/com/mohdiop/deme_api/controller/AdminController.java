package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final SchoolService schoolService;

    public AdminController(SchoolService schoolService) {
        this.schoolService = schoolService;
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
}

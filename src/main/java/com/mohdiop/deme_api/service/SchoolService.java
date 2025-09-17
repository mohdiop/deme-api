package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.repository.SchoolRepository;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolResponse createSchool(CreateSchoolRequest createSchoolRequest) {
        return schoolRepository.save(
                createSchoolRequest.toSchool()
        ).toResponse();
    }
}

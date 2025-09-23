package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public SchoolService(SchoolRepository schoolRepository, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }

    public SchoolResponse createSchool(CreateSchoolRequest createSchoolRequest) {
        if (createSchoolRequest.email() != null && userRepository.findByUserEmail(createSchoolRequest.email()).isPresent()) {
            throw new EntityExistsException("Email invalide!");
        }
        if (userRepository.findByUserPhone(createSchoolRequest.phone()).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide!");
        }
        return schoolRepository.save(
                createSchoolRequest.toSchool()
        ).toResponse();
    }

    public List<SchoolResponse> getAllSchool() {
        List<School> allSchools = schoolRepository.findAll();
        if (allSchools.isEmpty()) return new ArrayList<>();
        return allSchools.stream().map(School::toResponse).toList();
    }
}

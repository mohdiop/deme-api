package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateSchoolRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateSchoolRequest;
import com.mohdiop.deme_api.dto.response.SchoolResponse;
import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
            throw new EntityExistsException("Email invalide.");
        }
        if (userRepository.findByUserPhone(createSchoolRequest.phone()).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide.");
        }
        return schoolRepository.save(
                createSchoolRequest.toSchool()
        ).toResponse();
    }

    public SchoolResponse updateSchool(Long userId, UpdateSchoolRequest updateSchoolRequest) {
        validatePhoneAndEmailOrThrowException(updateSchoolRequest.phone(), updateSchoolRequest.email());
        School schoolToUpdate = schoolRepository.findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        if(updateSchoolRequest.identificationNumber() != null) {
            schoolToUpdate.setSchoolIdentificationNumber(updateSchoolRequest.identificationNumber());
        }
        if(updateSchoolRequest.name() != null) {
            schoolToUpdate.setSchoolName(updateSchoolRequest.name());
        }
        if(updateSchoolRequest.address() != null) {
            schoolToUpdate.setSchoolAddress(updateSchoolRequest.address());
        }
        if(updateSchoolRequest.type() != null) {
            schoolToUpdate.setSchoolType(updateSchoolRequest.type());
        }
        if (updateSchoolRequest.email() != null) {
            schoolToUpdate.setUserEmail(updateSchoolRequest.email());
        }
        if (updateSchoolRequest.phone() != null) {
            schoolToUpdate.setUserPhone(updateSchoolRequest.phone());
        }
        if (updateSchoolRequest.password() != null) {
            schoolToUpdate.setUserPassword(
                    BCrypt.hashpw(updateSchoolRequest.password(), BCrypt.gensalt())
            );
        }
        return schoolRepository.save(schoolToUpdate)
                .toResponse();
    }

    public List<SchoolResponse> getAllSchool() {
        List<School> allSchools = schoolRepository.findAll();
        if (allSchools.isEmpty()) return new ArrayList<>();
        return allSchools.stream().map(School::toResponse).toList();
    }

    public void validatePhoneAndEmailOrThrowException(String phone, String email) {
        if (email != null && userRepository.findByUserEmail(email).isPresent()) {
            throw new EntityExistsException("Email invalide.");
        }
        if (userRepository.findByUserPhone(phone).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide.");
        }
    }
}

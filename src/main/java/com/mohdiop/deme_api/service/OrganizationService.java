package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateOrganizationRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateOrganizationRequest;
import com.mohdiop.deme_api.dto.response.OrganizationResponse;
import com.mohdiop.deme_api.entity.Organization;
import com.mohdiop.deme_api.repository.OrganizationRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public OrganizationService(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public OrganizationResponse createOrganization(
            CreateOrganizationRequest createOrganizationRequest
    ) {
        validatePhoneAndEmailOrThrowException(createOrganizationRequest.phone(), createOrganizationRequest.email());
        return organizationRepository.save(createOrganizationRequest.toOrganization())
                .toResponse();
    }

    public OrganizationResponse updateOrganization(
            Long orgId,
            UpdateOrganizationRequest updateRequest
    ) {
        validatePhoneAndEmailOrThrowException(updateRequest.phone(), updateRequest.email());
        Organization orgToUpdate = organizationRepository.findById(orgId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Organisation introuvable.")
                );
        if (updateRequest.name() != null) {
            orgToUpdate.setOrganizationName(updateRequest.name());
        }
        if (updateRequest.address() != null) {
            orgToUpdate.setOrganizationAddress(updateRequest.address());
        }
        if (updateRequest.legalStatus() != null) {
            orgToUpdate.setOrganizationLegalStatus(updateRequest.legalStatus());
        }
        if (updateRequest.createdAt() != null) {
            orgToUpdate.setOrganizationCreatedAt(updateRequest.createdAt());
        }
        if (updateRequest.history() != null) {
            orgToUpdate.setOrganizationHistory(updateRequest.history());
        }
        if (updateRequest.interventionArea() != null) {
            orgToUpdate.setOrganizationInterventionArea(updateRequest.interventionArea());
        }
        if (updateRequest.targetAudiences() != null && !updateRequest.targetAudiences().isEmpty()) {
            orgToUpdate.setOrganizationTargetAudiences(updateRequest.targetAudiences());
        }
        if (updateRequest.webSite() != null) {
            orgToUpdate.setOrganizationWebSite(updateRequest.webSite());
        }
        if (updateRequest.phone() != null) {
            orgToUpdate.setUserPhone(updateRequest.phone());
        }
        if (updateRequest.email() != null) {
            orgToUpdate.setUserEmail(updateRequest.email());
        }
        if (updateRequest.password() != null) {
            orgToUpdate.setUserPassword(BCrypt.hashpw(updateRequest.password(), BCrypt.gensalt()));
        }
        if (updateRequest.pictureUrl() != null) {
            orgToUpdate.setPictureUrl(updateRequest.pictureUrl());
        }

        return organizationRepository.save(orgToUpdate).toResponse();
    }

    public List<OrganizationResponse> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        if (organizations.isEmpty()) return new ArrayList<>();
        return organizations.stream().map(Organization::toResponse).toList();
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

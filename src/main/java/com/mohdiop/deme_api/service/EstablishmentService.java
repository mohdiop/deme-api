package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateEstablishmentRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateEstablishmentRequest;
import com.mohdiop.deme_api.dto.response.EstablishmentResponse;
import com.mohdiop.deme_api.entity.Establishment;
import com.mohdiop.deme_api.repository.EstablishmentRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;

    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
    }

    public EstablishmentResponse createEstablishment(CreateEstablishmentRequest createEstablishmentRequest) {
        validateFieldsOrThrowException(createEstablishmentRequest.name(), createEstablishmentRequest.phone(), createEstablishmentRequest.email());
        return establishmentRepository.save(createEstablishmentRequest.toEstablishment()).toResponse();
    }

    public EstablishmentResponse updateEstablishment(Long establishmentId, UpdateEstablishmentRequest updateEstablishmentRequest) {
        validateFieldsOrThrowException(updateEstablishmentRequest.name(), updateEstablishmentRequest.phone(), updateEstablishmentRequest.email());
        Establishment establishmentToUpdate = establishmentRepository.findById(establishmentId).orElseThrow(() -> new EntityNotFoundException("Ecole introuvable."));
        if (updateEstablishmentRequest.name() != null) {
            establishmentToUpdate.setEstablishmentName(updateEstablishmentRequest.name());
        }
        if (updateEstablishmentRequest.phone() != null) {
            establishmentToUpdate.setEstablishmentPhoneNumber(updateEstablishmentRequest.phone());
        }
        if (updateEstablishmentRequest.email() != null) {
            establishmentToUpdate.setEstablishmentEmail(updateEstablishmentRequest.email());
        }
        if (updateEstablishmentRequest.address() != null) {
            establishmentToUpdate.setEstablishmentAddress(updateEstablishmentRequest.address());
        }
        if (updateEstablishmentRequest.identificationNumber() != null) {
            establishmentToUpdate.setEstablishmentIdentificationNumber(updateEstablishmentRequest.identificationNumber());
        }
        if (updateEstablishmentRequest.creationDate() != null) {
            establishmentToUpdate.setEstablishmentCreationDate(updateEstablishmentRequest.creationDate());
        }
        if (updateEstablishmentRequest.type() != null) {
            establishmentToUpdate.setEstablishmentType(updateEstablishmentRequest.type());
        }
        if (updateEstablishmentRequest.levels() != null) {
            establishmentToUpdate.setEstablishmentLevels(updateEstablishmentRequest.levels());
        }
        establishmentToUpdate.setLastUpdatedAt(LocalDateTime.now());
        return establishmentRepository.save(establishmentToUpdate).toResponse();
    }

    public List<EstablishmentResponse> getAllEstablishments() {
        List<Establishment> establishments = establishmentRepository.findAll();
        return establishments.stream().map(Establishment::toResponse).toList();
    }

    public void validateFieldsOrThrowException(String name, String phoneNumber, String email) {
        if (establishmentRepository.findByEstablishmentName(name).isPresent()) {
            throw new EntityExistsException("Nom d'école invalide.");
        }
        if (establishmentRepository.findByEstablishmentPhoneNumber(phoneNumber).isPresent()) {
            throw new EntityExistsException("Numéro de téléphone invalide.");
        }
        if (establishmentRepository.findByEstablishmentEmail(email).isPresent()) {
            throw new EntityExistsException("Email invalide.");
        }
    }
}

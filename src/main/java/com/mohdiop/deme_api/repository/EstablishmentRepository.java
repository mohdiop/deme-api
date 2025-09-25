package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    Optional<Establishment> findByEstablishmentName(String name);

    Optional<Establishment> findByEstablishmentPhoneNumber(String phoneNumber);

    Optional<Establishment> findByEstablishmentEmail(String email);
}

package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}

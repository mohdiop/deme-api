package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}

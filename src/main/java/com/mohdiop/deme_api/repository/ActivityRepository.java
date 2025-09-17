package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}

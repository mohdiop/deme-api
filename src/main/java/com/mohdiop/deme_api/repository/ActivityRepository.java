package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByStudentUserId(Long studentId);
}

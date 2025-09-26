package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    List<Tutor> findByStudentUserId(Long studentId);
}

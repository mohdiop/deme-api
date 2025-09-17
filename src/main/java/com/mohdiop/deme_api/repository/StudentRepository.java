package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

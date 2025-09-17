package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

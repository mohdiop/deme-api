package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByUserEmailOrUserPhone(String email, String phone);
}

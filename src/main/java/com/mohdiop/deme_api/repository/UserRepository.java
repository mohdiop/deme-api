package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);

    Optional<User> findByUserPhone(String phone);
}

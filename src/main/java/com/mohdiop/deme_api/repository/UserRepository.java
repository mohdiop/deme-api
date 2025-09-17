package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

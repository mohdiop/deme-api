package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.entity.*;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Object me(Long userId) {
        Object user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        if (user instanceof Admin) {
            return ((Admin) user).toResponse();
        }
        if (user instanceof Sponsor) {
            return ((Sponsor) user).toResponse();
        }
        if (user instanceof School) {
            return ((School) user).toResponse();
        }
        return ((Student) user).toResponse();
    }

    public List<Record> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) return new ArrayList<>();
        return allUsers.stream().map(
                (user) -> {
                    if (user instanceof Admin) {
                        return ((Admin) user).toResponse();
                    }
                    if (user instanceof Sponsor) {
                        return ((Sponsor) user).toResponse();
                    }
                    if (user instanceof School) {
                        return ((School) user).toResponse();
                    }
                    return ((Student) user).toResponse();
                }
        ).toList();
    }
}

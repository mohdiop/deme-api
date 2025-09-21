package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.entity.Admin;
import com.mohdiop.deme_api.entity.School;
import com.mohdiop.deme_api.entity.Sponsor;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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
}

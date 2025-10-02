package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.entity.*;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
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
        if (user instanceof Organization) {
            return ((Organization) user).toResponse();
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
                    if (user instanceof Organization) {
                        return ((Organization) user).toResponse();
                    }
                    return ((Student) user).toResponse();
                }
        ).toList();
    }

    public Record suspendUser(
            Long userId
    ) throws BadRequestException {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        if(user.getUserState() == UserState.SUSPENDED) {
            throw new BadRequestException("Utilisateur déjà suspendu.");
        }
        if (user.getUserRoles().contains(UserRole.ROLE_ADMIN)) {
            throw new BadRequestException("Impossible de suspendre un administrateur.");
        }
        user.setUserState(UserState.SUSPENDED);
        return switch (user) {
            case Admin admin -> userRepository.save(admin).toResponse();
            case Sponsor sponsor -> userRepository.save(sponsor).toResponse();
            case Organization organization -> userRepository.save(organization).toResponse();
            default -> userRepository.save((Student) user).toResponse();
        };
    }
}

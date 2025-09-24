package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateAdminRequest;
import com.mohdiop.deme_api.entity.Admin;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void initializeAdmin(CreateAdminRequest createAdminRequest) {
        List<Admin> potentialAdmins = adminRepository.findByUserEmailOrUserPhone(
                createAdminRequest.email(),
                createAdminRequest.phone()
        );
        if (!potentialAdmins.isEmpty()) {
            for (var potentialAdmin : potentialAdmins) {
                if (!potentialAdmin.getUserRoles().contains(UserRole.ROLE_ADMIN)) {
                    throw new IllegalArgumentException(
                            "Email ou téléphone non valide pour initialisation."
                    );
                }
            }
            return;
        }
        adminRepository.save(
                createAdminRequest.toEntity()
        );
    }
}

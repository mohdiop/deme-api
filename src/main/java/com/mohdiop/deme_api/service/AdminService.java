package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateAdminRequest;
import com.mohdiop.deme_api.dto.response.AdminResponse;
import com.mohdiop.deme_api.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public AdminResponse createAdmin(CreateAdminRequest createAdminRequest) {
        return adminRepository.save(
                createAdminRequest.toAdmin()
        ).toResponse();
    }
}

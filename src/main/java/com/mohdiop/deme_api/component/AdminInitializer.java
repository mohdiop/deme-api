package com.mohdiop.deme_api.component;

import com.mohdiop.deme_api.dto.request.CreateAdminRequest;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import com.mohdiop.deme_api.service.AdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AdminService adminService;

    public AdminInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {
        adminService.createAdmin(
                new CreateAdminRequest(
                        "70313104",
                        "mohdiopcode@gmail.com",
                        "12345678",
                        new HashSet<>(List.of(UserRole.ROLE_ADMIN)),
                        UserState.ACTIVE
                )
        );
    }
}

package com.mohdiop.deme_api.component;

import com.mohdiop.deme_api.dto.request.creation.CreateAdminRequest;
import com.mohdiop.deme_api.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AdminService adminService;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.phone}")
    private String adminPhone;

    public AdminInitializer(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {
        adminService.initializeAdmin(
                new CreateAdminRequest(
                        adminPhone,
                        adminEmail,
                        adminPassword
                )
        );
    }
}

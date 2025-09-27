package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.response.NotificationResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    public NotificationController(NotificationService notificationService, AuthenticationService authenticationService) {
        this.notificationService = notificationService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        return ResponseEntity.ok(
                notificationService.getNotificationsByUserId(
                        authenticationService.getCurrentUserId()
                )
        );
    }
}

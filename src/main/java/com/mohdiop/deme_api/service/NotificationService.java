package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.SendNotificationRequest;
import com.mohdiop.deme_api.dto.response.NotificationResponse;
import com.mohdiop.deme_api.entity.Notification;
import com.mohdiop.deme_api.entity.User;
import com.mohdiop.deme_api.repository.NotificationRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public NotificationResponse sendNotification(
            SendNotificationRequest sendNotificationRequest
    ) {
        User recipient = userRepository.findById(sendNotificationRequest.recipientId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        Notification notification = sendNotificationRequest.toRecipientlessNotification();
        notification.setRecipient(recipient);
        return notificationRepository.save(notification).toResponse();
    }

    public List<NotificationResponse> getNotificationsByUserId(
            Long userId
    ) {
        List<Notification> notifications = notificationRepository.findByRecipientUserId(userId);
        if (notifications.isEmpty()) return new ArrayList<>();
        return notifications.stream().map(Notification::toResponse).toList();
    }
}

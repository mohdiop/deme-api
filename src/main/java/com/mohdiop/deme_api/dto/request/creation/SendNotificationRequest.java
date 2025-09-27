package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Notification;

import java.time.LocalDateTime;

public record SendNotificationRequest(
        Long recipientId,
        String title,
        String content
) {
    public Notification toRecipientlessNotification() {
        return Notification.builder()
                .notificationTitle(title)
                .notificationContent(content)
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();
    }
}

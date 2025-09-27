package com.mohdiop.deme_api.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String content,
        Boolean isRead,
        LocalDateTime sentAt
) {
}

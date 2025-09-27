package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.NotificationResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String notificationTitle;

    @Column(nullable = false)
    private String notificationContent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "recipient_id")
    private User recipient;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private Boolean isRead = false;

    public NotificationResponse toResponse() {
        return new NotificationResponse(
                notificationId,
                notificationTitle,
                notificationContent,
                isRead,
                sentAt
        );
    }
}

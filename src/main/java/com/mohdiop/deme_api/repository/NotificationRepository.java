package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientUserId(Long recipientId);
}

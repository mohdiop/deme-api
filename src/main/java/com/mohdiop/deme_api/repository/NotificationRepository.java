package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

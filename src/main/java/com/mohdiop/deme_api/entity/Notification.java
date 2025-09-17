package com.mohdiop.deme_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
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
    private Boolean isRead = false;
}

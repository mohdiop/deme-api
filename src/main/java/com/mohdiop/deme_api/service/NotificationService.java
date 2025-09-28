package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.SendNotificationRequest;
import com.mohdiop.deme_api.dto.response.NotificationResponse;
import com.mohdiop.deme_api.entity.Notification;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.User;
import com.mohdiop.deme_api.entity.enumeration.StudentGender;
import com.mohdiop.deme_api.repository.NotificationRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void sendNotification(
            SendNotificationRequest sendNotificationRequest
    ) {
        User recipient = userRepository.findById(sendNotificationRequest.recipientId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        Notification notification = sendNotificationRequest.toRecipientlessNotification();
        notification.setRecipient(recipient);
        notificationRepository.save(notification).toResponse();
    }

    public List<NotificationResponse> getNotificationsByUserId(
            Long userId
    ) {
        List<Notification> notifications = notificationRepository.findByRecipientUserId(userId);
        if (notifications.isEmpty()) return new ArrayList<>();
        return notifications.stream().map(Notification::toResponse).toList();
    }

    public void sendSponsorshipExtendedNotifToOrg(
            Student student,
            LocalDateTime endAt
    ) {
        String title = "Parrainage prolongé.";
        String content = String.format(
                "Le parrainage de votre élève %s %s est prolongé jusqu'au %s",
                student.getStudentFirstName(),
                student.getStudentLastName(),
                endAt.format(formatter)
        );
        sendNotification(
                new SendNotificationRequest(
                        student.getOrganization().getUserId(),
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipExtendedNotifToStudent(
            Long studentId,
            LocalDateTime endAt
    ) {
        String title = "Parrainage prolongé";
        String content = String.format(
                "Votre parrainage a été prolongé jusqu'au %s.",
                endAt.format(formatter)
        );
        sendNotification(
                new SendNotificationRequest(
                        studentId,
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipStartedNotifToOrg(
            Sponsorship sponsorship
    ) {
        String sponsoredText = getSponsoredText(sponsorship.getStudent().getStudentGender());
        String title = String.format("Votre élève est %s.", sponsoredText);
        String content = "";
        switch (sponsorship.getSponsorshipType()) {
            case ANONYMOUS -> content = String.format(
                    "Votre élève %s %s a reçu un parrainage anonyme du %s au %s.",
                    sponsorship.getStudent().getStudentFirstName(),
                    sponsorship.getStudent().getStudentLastName(),
                    sponsorship.getSponsorshipStartedAt().format(formatter),
                    sponsorship.getSponsorshipEndAt().format(formatter)
            );
            case IDENTIFIED -> content = String.format(
                    "Votre élève %s %s est maintenant %s par %s %s du %s au %s.",
                    sponsorship.getStudent().getStudentFirstName(),
                    sponsorship.getStudent().getStudentLastName(),
                    sponsoredText,
                    sponsorship.getSponsor().getSponsorFirstName(),
                    sponsorship.getSponsor().getSponsorLastName(),
                    sponsorship.getSponsorshipStartedAt().format(formatter),
                    sponsorship.getSponsorshipEndAt().format(formatter)
            );
        }
        sendNotification(
                new SendNotificationRequest(
                        sponsorship.getStudent().getOrganization().getUserId(),
                        title,
                        content
                )
        );
    }

    public void sendSponsorshipStartedNotifToStudent(
            Sponsorship sponsorship
    ) {
        String title = "Nouveau parrainage.";
        String content = "";
        switch (sponsorship.getSponsorshipType()) {
            case ANONYMOUS -> content = String.format(
                    "Vous êtes maintenant %s.",
                    getSponsoredText(
                            sponsorship.getStudent().getStudentGender()
                    ));
            case IDENTIFIED -> content = String.format(
                    "Vous êtes maintenant %s par %s %s.",
                    getSponsoredText(
                            sponsorship.getStudent().getStudentGender()
                    ),
                    sponsorship.getSponsor().getSponsorFirstName(),
                    sponsorship.getSponsor().getSponsorLastName()
            );
        }
        sendNotification(
                new SendNotificationRequest(
                        sponsorship.getStudent().getUserId(),
                        title,
                        content
                )
        );
    }

    public String getSponsoredText(
            StudentGender gender
    ) {
        return (gender == StudentGender.FEMALE)
                ? "parrainée"
                : "parrainé";
    }
}

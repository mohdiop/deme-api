package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.ReportCard;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UploadReportCardRequest(
        @NotNull(message = "Le fichier du bulletin est obligatoire.")
        MultipartFile file,

        @NotNull(message = "La date d'émission du bulletin est obligatoire.")
        @PastOrPresent(message = "La date d'émission doit être dans le passé ou aujourd'hui")
        LocalDate emittedAt
) {

    public ReportCard toReportCard() {
        return ReportCard.builder()
                .reportCardId(null)
                .reportCardEmittedAt(emittedAt)
                .reportCardFileSizeMo(
                        file.getSize() / (1024D * 1024D)
                )
                .reportCardCreatedAt(LocalDateTime.now())
                .build();
    }
}

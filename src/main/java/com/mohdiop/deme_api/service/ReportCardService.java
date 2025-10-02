package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.UploadReportCardRequest;
import com.mohdiop.deme_api.dto.response.ReportCardResponse;
import com.mohdiop.deme_api.entity.ReportCard;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.repository.ReportCardRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.apache.tika.Tika;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportCardService {

    private final ReportCardRepository reportCardRepository;
    private final UploadService uploadService;
    private final StudentRepository studentRepository;
    private final SponsorshipRepository sponsorshipRepository;

    public ReportCardService(ReportCardRepository reportCardRepository, UploadService uploadService, StudentRepository studentRepository, SponsorshipRepository sponsorshipRepository) {
        this.reportCardRepository = reportCardRepository;
        this.uploadService = uploadService;
        this.studentRepository = studentRepository;
        this.sponsorshipRepository = sponsorshipRepository;
    }

    public ReportCardResponse uploadReportCard(
            Long studentId,
            UploadReportCardRequest uploadReportCardRequest
    ) throws BadRequestException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        if (reportCardRepository.findByStudentUserIdAndReportCardEmittedAt(
                studentId,
                uploadReportCardRequest.emittedAt()
        ).isPresent()) {
            throw new BadRequestException("Un bulletin existe déjà pour cette date.");
        }
        Tika tika = new Tika();
        String detectedType;
        try {
            detectedType = tika.detect(uploadReportCardRequest.file().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Un problème est survenu de notre côté, veuillez réessayer plus tard.");
        }
        if (!("application/pdf".equals(detectedType) ||
                "image/jpeg".equals(detectedType) ||
                "image/png".equals(detectedType))) {
            throw new IllegalArgumentException("Type de fichier non supporté : " + detectedType);
        }
        FileType fileType;
        if ("application/pdf".equals(detectedType)) {
            fileType = FileType.PDF;
        } else if ("image/jpeg".equals(detectedType)) {
            fileType = FileType.JPG;
        } else {
            fileType = FileType.PNG;
        }
        ReportCard reportCard = uploadReportCardRequest.toReportCard();
        String url = uploadService.uploadFile(
                uploadReportCardRequest.file(),
                UUID.randomUUID().toString(),
                null,
                fileType,
                false
        );
        reportCard.setStudent(student);
        reportCard.setReportCardUrl(url);
        reportCard.setReportCardFileType(fileType);
        reportCard.setReportCardLevel(
                student.getStudentLevel()
        );
        return reportCardRepository.save(reportCard).toResponse();
    }

    public List<ReportCardResponse> getStudentReportCards(
            Long studentId
    ) {
        Optional<Sponsorship> sponsorship = sponsorshipRepository.findByStudentUserId(studentId);
        if (sponsorship.isPresent()) {
            if (!sponsorship.get().getStudentInfoAccessible()) {
                throw new AccessDeniedException("Informations non partagé par l'élève.");
            }
        }
        var allReports = reportCardRepository.findByStudentUserId(studentId);
        if (allReports.isEmpty()) return new ArrayList<>();
        return allReports.stream().map(ReportCard::toResponse).toList();
    }
}

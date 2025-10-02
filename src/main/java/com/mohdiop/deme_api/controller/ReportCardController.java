package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.UploadReportCardRequest;
import com.mohdiop.deme_api.dto.response.ReportCardResponse;
import com.mohdiop.deme_api.service.ReportCardService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReportCardController {

    private final ReportCardService reportCardService;

    public ReportCardController(ReportCardService reportCardService) {
        this.reportCardService = reportCardService;
    }

    @PostMapping("/organizations/students/{studentId}/report-cards")
    public ResponseEntity<ReportCardResponse> uploadReportCard(
            @PathVariable Long studentId,
            @Valid @ModelAttribute UploadReportCardRequest uploadReportCardRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                reportCardService.uploadReportCard(
                        studentId,
                        uploadReportCardRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @GetMapping("/report-cards")
    public ResponseEntity<List<ReportCardResponse>> getStudentReportCards(
            @RequestParam("studentId") Long studentId
    ) {
        return ResponseEntity.ok(
                reportCardService.getStudentReportCards(
                        studentId
                )
        );
    }
}

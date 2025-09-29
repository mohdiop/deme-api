package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.MakeReportRequest;
import com.mohdiop.deme_api.dto.request.update.UpdateReportStateRequest;
import com.mohdiop.deme_api.dto.response.ReportResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.ReportService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sponsorships/expenses")
public class ReportController {

    private final ReportService reportService;
    private final AuthenticationService authenticationService;

    public ReportController(ReportService reportService, AuthenticationService authenticationService) {
        this.reportService = reportService;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{expenseId}/reports")
    public ResponseEntity<ReportResponse> makeReport(
            @PathVariable Long expenseId,
            @Valid @RequestBody MakeReportRequest makeReportRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                reportService.makeReport(
                        authenticationService.getCurrentUserId(),
                        expenseId,
                        makeReportRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @PostMapping("/reports/{reportId}/state")
    public ResponseEntity<ReportResponse> setState(
            @PathVariable Long reportId,
            @Valid @RequestBody UpdateReportStateRequest updateReportStateRequest
    ) throws BadRequestException {
        return ResponseEntity.ok(
                reportService.setReportState(reportId, updateReportStateRequest.state())
        );
    }
}

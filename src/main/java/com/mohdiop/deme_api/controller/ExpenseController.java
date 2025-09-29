package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateExpenseRequest;
import com.mohdiop.deme_api.dto.response.ExpenseResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.ExpenseService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sponsorships")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AuthenticationService authenticationService;

    public ExpenseController(ExpenseService expenseService, AuthenticationService authenticationService) {
        this.expenseService = expenseService;
        this.authenticationService = authenticationService;
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping("/{sponsorshipId}/expenses")
    public ResponseEntity<ExpenseResponse> createExpense(
            @PathVariable Long sponsorshipId,
            @Valid @ModelAttribute CreateExpenseRequest createExpenseRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                expenseService.createExpense(
                        sponsorshipId,
                        createExpenseRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasAnyRole('ORGANIZATION', 'SPONSOR', 'STUDENT')")
    @GetMapping("/{sponsorshipId}/expenses")
    public ResponseEntity<List<ExpenseResponse>> getSponsorshipExpenses(
            @PathVariable Long sponsorshipId
    ) {
        return ResponseEntity.ok(expenseService.getSponsorshipsExpenses(sponsorshipId));
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponse>> getAllMyExpenses() {
        return ResponseEntity.ok(
                expenseService.getSponsorExpenses(
                        authenticationService.getCurrentUserId()
                )
        );
    }
}

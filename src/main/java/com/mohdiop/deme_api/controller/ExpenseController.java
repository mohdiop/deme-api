package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateExpenseRequest;
import com.mohdiop.deme_api.dto.response.ExpenseResponse;
import com.mohdiop.deme_api.service.ExpenseService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @ModelAttribute CreateExpenseRequest createExpenseRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                expenseService.createExpense(createExpenseRequest),
                HttpStatus.CREATED
        );
    }
}

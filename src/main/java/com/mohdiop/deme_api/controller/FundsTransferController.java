package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.MakeFundsTransferRequest;
import com.mohdiop.deme_api.dto.request.creation.ValidateFundsTransferRequest;
import com.mohdiop.deme_api.dto.response.FundsTransferResponse;
import com.mohdiop.deme_api.service.AuthenticationService;
import com.mohdiop.deme_api.service.FundsTransferService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/funds-transfer")
public class FundsTransferController {

    private final AuthenticationService authenticationService;
    private final FundsTransferService fundsTransferService;

    public FundsTransferController(AuthenticationService authenticationService, FundsTransferService fundsTransferService) {
        this.authenticationService = authenticationService;
        this.fundsTransferService = fundsTransferService;
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping
    public ResponseEntity<FundsTransferResponse> makeTransfer(
            @Valid @RequestBody MakeFundsTransferRequest makeFundsTransferRequest
    ) throws BadRequestException {
        return new ResponseEntity<>(
                fundsTransferService.makeTransfer(
                        authenticationService.getCurrentUserId(),
                        makeFundsTransferRequest
                ),
                HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('SPONSOR')")
    @PostMapping("/{transferId}/validate")
    public ResponseEntity<FundsTransferResponse> validateTransfer(
            @PathVariable Long transferId,
            @Valid @RequestBody ValidateFundsTransferRequest validateFundsTransferRequest
    ) throws BadRequestException {
        return ResponseEntity.ok(fundsTransferService.validateTransfer(
                        transferId,
                        validateFundsTransferRequest.toStudent()
                )
        );
    }

    @PreAuthorize("hasAnyRole('SPONSOR', 'ORGANIZATION')")
    @PostMapping("/{transferId}/cancel")
    public ResponseEntity<FundsTransferResponse> cancelTransfer(
            @PathVariable Long transferId
    ) throws BadRequestException {
        return ResponseEntity.ok(fundsTransferService.cancelTransfer(transferId));
    }
}

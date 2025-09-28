package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.MakePaymentRequest;
import com.mohdiop.deme_api.dto.response.PaymentResponse;
import com.mohdiop.deme_api.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SPONSOR')")
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody MakePaymentRequest makePaymentRequest
    ) {
        return new ResponseEntity<>(
                paymentService.createPayment(makePaymentRequest),
                HttpStatus.CREATED
        );
    }
}

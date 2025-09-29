package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.MakePaymentRequest;
import com.mohdiop.deme_api.dto.response.PaymentResponse;
import com.mohdiop.deme_api.entity.Payment;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import com.mohdiop.deme_api.repository.PaymentRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SponsorshipRepository sponsorshipRepository;

    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository, SponsorshipRepository sponsorshipRepository, NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.sponsorshipRepository = sponsorshipRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public PaymentResponse createPayment(
            MakePaymentRequest makePaymentRequest
    ) {
        Sponsorship sponsorship = sponsorshipRepository.findById(makePaymentRequest.sponsorshipId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        Payment payment = makePaymentRequest.toSonsorshiplessPayment();
        if (sponsorship.getSponsorshipState() == SponsorshipState.PENDING) {
            sponsorship.setSponsorshipState(SponsorshipState.IN_PROGRESS);
            notificationService.sendSponsorshipStartedNotifToOrg(sponsorship, makePaymentRequest.equivalenceXOF());
            notificationService.sendSponsorshipStartedNotifToStudent(sponsorship);
        }
        Double currentFunds = sponsorship.getStudent().getStudentFunds();
        sponsorship.getStudent().setStudentFunds(
                currentFunds + makePaymentRequest.equivalenceXOF()
        );
        sponsorshipRepository.save(sponsorship);
        payment.setSponsorship(sponsorship);
        return paymentRepository.save(payment).toResponse();
    }

    public List<PaymentResponse> getAllPayments() {
        List<Payment> allPayments = paymentRepository.findAll();
        if (allPayments.isEmpty()) return new ArrayList<>();
        return allPayments.stream().map(Payment::toResponse).toList();
    }
}

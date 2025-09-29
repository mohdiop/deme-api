package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.MakeFundsTransferRequest;
import com.mohdiop.deme_api.dto.response.FundsTransferResponse;
import com.mohdiop.deme_api.entity.FundsTransfer;
import com.mohdiop.deme_api.entity.Organization;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.entity.enumeration.NeedState;
import com.mohdiop.deme_api.entity.enumeration.TransferState;
import com.mohdiop.deme_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FundsTransferService {

    private final FundsTransferRepository fundsTransferRepository;
    private final StudentRepository studentRepository;
    private final OrganizationRepository organizationRepository;
    private final NeedRepository needRepository;
    private final NotificationService notificationService;
    private final SponsorshipRepository sponsorshipRepository;

    public FundsTransferService(FundsTransferRepository fundsTransferRepository, StudentRepository studentRepository, OrganizationRepository organizationRepository, NeedRepository needRepository, NotificationService notificationService, SponsorshipRepository sponsorshipRepository) {
        this.fundsTransferRepository = fundsTransferRepository;
        this.studentRepository = studentRepository;
        this.organizationRepository = organizationRepository;
        this.needRepository = needRepository;
        this.notificationService = notificationService;
        this.sponsorshipRepository = sponsorshipRepository;
    }

    @Transactional
    public FundsTransferResponse makeTransfer(Long authorId, MakeFundsTransferRequest makeFundsTransferRequest) throws BadRequestException {
        if (!needRepository.findByStudentUserIdAndNeedState(
                makeFundsTransferRequest.fromStudent(),
                NeedState.UNSATISFIED
        ).isEmpty()) {
            throw new BadRequestException("Transfert impossible, cet(te) élève a des besoins à satisfaire.");
        }
        Organization author = organizationRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Organisation introuvable."));
        Student fromStudent = studentRepository.findById(makeFundsTransferRequest.fromStudent()).orElseThrow(() -> new EntityNotFoundException("Elève expéditeur introuvable."));
        if (!author.getUserId().equals(fromStudent.getOrganization().getUserId())) {
            throw new AccessDeniedException("L'élève expéditeur n'est pas affilié(e) à cette organisation.");
        }
        if (fromStudent.getStudentFunds() < 5000) {
            throw new BadRequestException("Les transferts sont possibles qu'au délà de 5000 FCFA");
        }
        if (makeFundsTransferRequest.amount() < 5000) {
            throw new BadRequestException("Les transferts sont possibles qu'au délà de 5000 FCFA");
        }
        if (fromStudent.getStudentFunds() < makeFundsTransferRequest.amount()) {
            throw new BadRequestException("Les fonds de l'élève expéditeur sont insuffisants pour ce transfert.");
        }
        FundsTransfer fundsTransfer = makeFundsTransferRequest.toFundsTransfer();
        fundsTransfer.setAuthor(author);
        fundsTransfer.setFromStudent(fromStudent);
        Optional<Sponsorship> sponsorship = sponsorshipRepository.findByStudentUserId(fundsTransfer.getFromStudent().getUserId());
        sponsorship.ifPresent(value -> notificationService.sendPendingTransferNotifToSponsor(
                value.getSponsor().getUserId(),
                fundsTransfer
        ));
        return fundsTransferRepository.save(fundsTransfer).toResponse();
    }

    @Transactional
    public FundsTransferResponse validateTransfer(
            Long transferId,
            Long toStudentId
    ) throws BadRequestException {
        FundsTransfer fundsTransfer = fundsTransferRepository.findById(transferId).orElseThrow(() -> new EntityNotFoundException("Transfert introuvable."));
        if (fundsTransfer.getTransferState() == TransferState.CANCEL) {
            throw new BadRequestException("Impossible de valider un transfert annulé.");
        }
        if (fundsTransfer.getTransferState() == TransferState.VALIDATED) {
            throw new BadRequestException("Transfert déjà validé.");
        }
        if (fundsTransfer.getFromStudent().getUserId().equals(toStudentId)) {
            throw new BadRequestException("Les élèves expéditeur et destinataire doivent être différents.");
        }
        Student toStudent = studentRepository.findById(toStudentId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Elève introuvable.")
                );
        fundsTransfer.getFromStudent().setStudentFunds(fundsTransfer.getFromStudent().getStudentFunds() - fundsTransfer.getTransferAmount());
        toStudent.setStudentFunds(toStudent.getStudentFunds() + fundsTransfer.getTransferAmount());
        fundsTransfer.setTransferAcceptedAt(LocalDateTime.now());
        fundsTransfer.setTransferState(TransferState.VALIDATED);
        fundsTransfer.setToStudent(toStudent);
        return fundsTransferRepository.save(fundsTransfer).toResponse();
    }

    public FundsTransferResponse cancelTransfer(Long transferId) throws BadRequestException {
        FundsTransfer fundsTransfer = fundsTransferRepository.findById(transferId).orElseThrow(() -> new EntityNotFoundException("Transfert introuvable."));
        if (fundsTransfer.getTransferState() == TransferState.CANCEL) {
            throw new BadRequestException("Transfert déjà annulé.");
        }
        if (fundsTransfer.getTransferState() == TransferState.VALIDATED) {
            throw new BadRequestException("Impossible d'annuler un transfert déjà validé.");
        }
        fundsTransfer.setTransferState(TransferState.CANCEL);
        return fundsTransferRepository.save(fundsTransfer).toResponse();
    }
}

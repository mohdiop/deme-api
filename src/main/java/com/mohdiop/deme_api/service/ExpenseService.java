package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateExpenseRequest;
import com.mohdiop.deme_api.dto.response.ExpenseResponse;
import com.mohdiop.deme_api.entity.Expense;
import com.mohdiop.deme_api.entity.Need;
import com.mohdiop.deme_api.entity.Proof;
import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.enumeration.NeedState;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import com.mohdiop.deme_api.repository.ExpenseRepository;
import com.mohdiop.deme_api.repository.NeedRepository;
import com.mohdiop.deme_api.repository.SponsorshipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final SponsorshipRepository sponsorshipRepository;
    private final NeedRepository needRepository;

    private final ProofService proofService;

    public ExpenseService(ExpenseRepository expenseRepository, SponsorshipRepository sponsorshipRepository, NeedRepository needRepository, ProofService proofService) {
        this.expenseRepository = expenseRepository;
        this.sponsorshipRepository = sponsorshipRepository;
        this.needRepository = needRepository;
        this.proofService = proofService;
    }

    @Transactional
    public ExpenseResponse createExpense(
            CreateExpenseRequest createExpenseRequest
    ) throws BadRequestException {
        Sponsorship sponsorship = sponsorshipRepository.findById(createExpenseRequest.sponsorshipId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Parrainage introuvable.")
                );
        Need need = needRepository.findById(createExpenseRequest.needId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Besoin introuvable.")
                );
        if (!Objects.equals(need.getNeededAmount(), createExpenseRequest.amount())) {
            throw new BadRequestException(
                    String.format("Ce montant est insuffisant, montant nécessaire: %s", need.getNeededAmount().toString())
            );
        }
        if (createExpenseRequest.amount() > sponsorship.getStudent().getStudentFunds()) {
            throw new BadRequestException("Fond de l'élève insuffisant pour faire cette dépense.");
        }
        if (need.getNeedState() != NeedState.UNSATISFIED) {
            throw new BadRequestException("Impossible de faire une dépense pour ce besoin.");
        }
        sponsorship.getStudent().setStudentFunds(
                sponsorship.getStudent().getStudentFunds() - createExpenseRequest.amount()
        );
        need.setNeedState(NeedState.SATISFIED);
        Proof proof = proofService.uploadProof(
                createExpenseRequest.proof(),
                ProofType.EXPENSE
        );
        Expense expense = createExpenseRequest.toExpense();
        expense.setSponsorship(sponsorship);
        expense.setNeedToSatisfy(need);
        expense.setProof(proof);
        return expenseRepository.save(expense).toResponse();
    }
}

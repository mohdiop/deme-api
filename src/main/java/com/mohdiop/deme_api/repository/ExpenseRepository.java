package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findBySponsorshipSponsorshipId(Long sponsorshipId);

    List<Expense> findBySponsorshipSponsorUserId(Long sponsorId);
}

package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}

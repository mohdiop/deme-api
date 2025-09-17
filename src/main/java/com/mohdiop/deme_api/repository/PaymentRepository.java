package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

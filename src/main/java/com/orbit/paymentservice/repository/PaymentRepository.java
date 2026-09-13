package com.orbit.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orbit.paymentservice.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	Optional<Payment> findByOrderId(Long orderId);
}

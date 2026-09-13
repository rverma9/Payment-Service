package com.orbit.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.orbit.paymentservice.dto.PaymentResponseDto;
import com.orbit.paymentservice.exception.PaymentNotFoundException;
import com.orbit.paymentservice.service.PaymentService;

@RestController
public class OrderPaymentController {

    private final PaymentService paymentService;

    public OrderPaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@GetMapping("/api/orders/{orderId}/payment")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable Long orderId) throws PaymentNotFoundException {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}

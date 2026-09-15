package com.orbit.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orbit.paymentservice.dto.PaymentRequestDto;
import com.orbit.paymentservice.dto.PaymentResponseDto;
import com.orbit.paymentservice.exception.PaymentNotFoundException;
import com.orbit.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService) {
		this.paymentService=paymentService;
	}

    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestHeader("X-User-Id") String userId, @RequestBody PaymentRequestDto request) {
        return ResponseEntity.ok(paymentService.createPayment(userId,request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long id) throws PaymentNotFoundException {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDto> refundPayment(@PathVariable Long id) throws PaymentNotFoundException {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}

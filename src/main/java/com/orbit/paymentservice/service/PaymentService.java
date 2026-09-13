package com.orbit.paymentservice.service;

import com.orbit.paymentservice.dto.PaymentRequestDto;
import com.orbit.paymentservice.dto.PaymentResponseDto;
import com.orbit.paymentservice.exception.PaymentNotFoundException;

public interface PaymentService {

	PaymentResponseDto createPayment(PaymentRequestDto request);

    PaymentResponseDto getPaymentById(Long id) throws PaymentNotFoundException;

    PaymentResponseDto getPaymentByOrderId(Long orderId) throws PaymentNotFoundException;

    PaymentResponseDto refundPayment(Long id) throws PaymentNotFoundException;
}

package com.orbit.paymentservice.service;

import org.springframework.stereotype.Service;

import com.orbit.paymentservice.dto.PaymentRequestDto;
import com.orbit.paymentservice.dto.PaymentResponseDto;
import com.orbit.paymentservice.exception.PaymentNotFoundException;
import com.orbit.paymentservice.model.Payment;
import com.orbit.paymentservice.model.PaymentStatus;
import com.orbit.paymentservice.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService{


    private final PaymentRepository paymentRepository;
    
    

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
		super();
		this.paymentRepository = paymentRepository;
	}

	@Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);

        return mapToDto(
                paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentById(Long id) throws PaymentNotFoundException {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return mapToDto(payment);
    }

    @Override
    public PaymentResponseDto getPaymentByOrderId(Long orderId) throws PaymentNotFoundException {

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found"));

        return mapToDto(payment);
    }

    @Override
    public PaymentResponseDto refundPayment(Long id) throws PaymentNotFoundException {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->new PaymentNotFoundException("Payment not found"));
        payment.setStatus(PaymentStatus.REFUNDED);

        return mapToDto(paymentRepository.save(payment));
    }

    private PaymentResponseDto mapToDto(Payment payment) {

        PaymentResponseDto dto = new PaymentResponseDto();

        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());

        return dto;
    }
}

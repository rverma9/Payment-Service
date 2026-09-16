package com.orbit.paymentservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbit.paymentservice.client.OrderClient;
import com.orbit.paymentservice.dto.OrderResponseDto;
import com.orbit.paymentservice.dto.PaymentRequestDto;
import com.orbit.paymentservice.dto.PaymentResponseDto;
import com.orbit.paymentservice.exception.PaymentNotFoundException;
import com.orbit.paymentservice.model.Payment;
import com.orbit.paymentservice.model.PaymentStatus;
import com.orbit.paymentservice.producer.PaymentProducer;
import com.orbit.paymentservice.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService{


    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentProducer paymentProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    

    public PaymentServiceImpl(PaymentRepository paymentRepository,OrderClient orderClient, PaymentProducer paymentProducer) {
		this.paymentRepository = paymentRepository;
		this.orderClient=orderClient;
		this.paymentProducer=paymentProducer;
	}

	@Override
	@Transactional
    public PaymentResponseDto createPayment(String userId, PaymentRequestDto request) {

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);

        Payment savedPayment = paymentRepository.save(payment);
        
        OrderResponseDto order = null;
        try {
            order = orderClient.getOrderById(request.getOrderId());
        } catch (Exception e) {
            System.err.println("Warning: Could not fetch order details from Order-Service: " + e.getMessage());
        }
        
        try {
            Map<String, Object> eventPayload = new HashMap<>();
            eventPayload.put("eventType", "PAYMENT_SUCCESS");
            eventPayload.put("userId", userId);
            eventPayload.put("orderId", savedPayment.getOrderId());
            eventPayload.put("paymentId", savedPayment.getId());
            eventPayload.put("amount", savedPayment.getAmount());
            eventPayload.put("paymentMethod", savedPayment.getPaymentMethod());
            eventPayload.put("message", "Payment completed successfully for Order #" + savedPayment.getOrderId());

            if (order != null && order.getItems() != null) {
                eventPayload.put("items", order.getItems());
            }

            String jsonMessage = objectMapper.writeValueAsString(eventPayload);

            paymentProducer.sendPaymentEvent(String.valueOf(savedPayment.getOrderId()), jsonMessage);

        } catch (Exception e) {
            System.err.println("Failed to publish Kafka payment event: " + e.getMessage());
        }
        
        return mapToDto(savedPayment);
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

        Payment refundedPayment = paymentRepository.save(payment);

        OrderResponseDto order = null;
        try {
            order = orderClient.getOrderById(refundedPayment.getOrderId());
        } catch (Exception e) {
            System.err.println("Warning: Could not fetch order details for refund: " + e.getMessage());
        }

        try {
            Map<String, Object> eventPayload = new HashMap<>();
            eventPayload.put("eventType", "PAYMENT_REFUNDED");
            eventPayload.put("orderId", refundedPayment.getOrderId());
            eventPayload.put("paymentId", refundedPayment.getId());
            eventPayload.put("amount", refundedPayment.getAmount());
            eventPayload.put("message", "Payment refunded for Order #" + refundedPayment.getOrderId());

            if (order != null && order.getItems() != null) {
                eventPayload.put("items", order.getItems());
            }

            String jsonMessage = objectMapper.writeValueAsString(eventPayload);
            paymentProducer.sendPaymentEvent(String.valueOf(refundedPayment.getOrderId()), jsonMessage);

        } catch (Exception e) {
            System.err.println("Failed to publish Kafka refund event: " + e.getMessage());
        }

        return mapToDto(refundedPayment);
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

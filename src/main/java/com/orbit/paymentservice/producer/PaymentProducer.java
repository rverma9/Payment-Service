package com.orbit.paymentservice.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPaymentEvent(String orderId, String message) {
        kafkaTemplate.send("payment-events", orderId, message);
        System.out.println("Kafka event sent for Order #" + orderId);
    }
}
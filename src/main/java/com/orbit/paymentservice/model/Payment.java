package com.orbit.paymentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Long orderId;

	    private Double amount;

	    private String paymentMethod;

	    @Enumerated(EnumType.STRING)
	    private PaymentStatus status;
	    
	    public Payment() {
	    	
	    }

		public Payment(Long id, Long orderId, Double amount, String paymentMethod, PaymentStatus status) {
			super();
			this.id = id;
			this.orderId = orderId;
			this.amount = amount;
			this.paymentMethod = paymentMethod;
			this.status = status;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getOrderId() {
			return orderId;
		}

		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public PaymentStatus getStatus() {
			return status;
		}

		public void setStatus(PaymentStatus status) {
			this.status = status;
		}

}

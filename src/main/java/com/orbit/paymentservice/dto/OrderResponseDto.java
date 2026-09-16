package com.orbit.paymentservice.dto;

import java.util.List;

public class OrderResponseDto {
    private Long id;
    private Long orderNumber;
    private Double totalAmount;
    private String status;
    private List<OrderItemResponseDto> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderNumber() { return orderNumber; }
    public void setOrderNumber(Long orderNumber) { this.orderNumber = orderNumber; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItemResponseDto> getItems() { return items; }
    public void setItems(List<OrderItemResponseDto> items) { this.items = items; }
}
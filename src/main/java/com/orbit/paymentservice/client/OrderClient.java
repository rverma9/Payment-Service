package com.orbit.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.orbit.paymentservice.dto.OrderResponseDto;

@FeignClient(name = "order-service", path = "/api/orders")
public interface OrderClient {

    @GetMapping("/{id}")
    OrderResponseDto getOrderById(@PathVariable("id") Long id);
}
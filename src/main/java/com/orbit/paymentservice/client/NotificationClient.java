package com.orbit.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.orbit.paymentservice.dto.NotificationRequestDto;
import com.orbit.paymentservice.dto.NotificationResponseDto;

@FeignClient(name="NOTIFICATION-SERVICE")
public interface NotificationClient {
	
	@PostMapping("/api/notifications")
	NotificationResponseDto createNotification(@RequestBody NotificationRequestDto request);
	

}

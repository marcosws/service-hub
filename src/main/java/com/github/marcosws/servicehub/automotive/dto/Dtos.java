package com.github.marcosws.servicehub.automotive.dto;

import java.time.LocalDateTime;

public class Dtos {
	
	
	public record SchedulingRequestDto(
			Long vehicleId, 
			Long serviceId, 
			LocalDateTime scheduledDateTime
	) {}
	
	public record SchedulingResponseDto(
			Long id, 
			String clientName,
			String vehicleModel,
			String vehiclePlate,
			String serviceName,
			LocalDateTime scheduledDateTime,
			String status
	) {}
	
	public record LoginRequestDto(
			String email,
			String password
	) {}
	
	public record TokenResponseDto(
			String token,
			String type
	) {}
	

}

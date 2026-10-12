package com.github.marcosws.servicehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingRequestDto;
import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingResponseDto;
import com.github.marcosws.servicehub.service.SchedulingService;


@RestController
@RequestMapping("/api/schedulings")
public class SchedulingController {
	
	private final SchedulingService schedulingService;
	
	public SchedulingController(SchedulingService schedulingService) {
		this.schedulingService = schedulingService;
	}
	
	@PostMapping
	public ResponseEntity<SchedulingResponseDto> createScheduling(
			@RequestParam Long clientId,
			@Valid @RequestBody SchedulingRequestDto requestDto
			) {
		SchedulingResponseDto response = schedulingService.createScheduling(clientId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<SchedulingResponseDto>> listForClient(@PathVariable Long clientId) {
		List<SchedulingResponseDto> schedulings = schedulingService.listCustomerScheduling(clientId);
		return ResponseEntity.ok(schedulings);

	}
	
	@PatchMapping("{id}/cancel")
	public ResponseEntity<Void> cancelScheduling(@PathVariable Long clientId, @RequestParam Long schedulingId) {
		schedulingService.cancelScheduling(clientId, schedulingId);
		return ResponseEntity.noContent().build();
	}
	
	

}

package com.github.marcosws.servicehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleRequestDto;
import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleResponseDto;
import com.github.marcosws.servicehub.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	
	private final VehicleService vehicleService;
	
	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	@PostMapping
	public ResponseEntity<VehicleResponseDto> registerVehicle(@Valid @RequestBody VehicleRequestDto request){
		VehicleResponseDto response = vehicleService.registerVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<VehicleResponseDto>> getVehiclesByClientId(Long clientId) {
        List<VehicleResponseDto> vehicles = vehicleService.findVehiclesByClientId(clientId);
        return ResponseEntity.ok(vehicles);
	}
	

}

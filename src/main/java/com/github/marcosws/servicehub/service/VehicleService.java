package com.github.marcosws.servicehub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.domain.Vehicle;
import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleRequestDto;
import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleResponseDto;
import com.github.marcosws.servicehub.exception.BusinessRulesException;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.repository.VehicleRepository;

@Service
public class VehicleService {

	private final VehicleRepository vehicleRepository;
	private final UserRepository userRepository;
	
	public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository) {
		this.vehicleRepository = vehicleRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public VehicleResponseDto registerVehicle(VehicleRequestDto request) {
		
		User owner = userRepository.findById(request.ownerId())
				.orElseThrow(() -> new BusinessRulesException("Proprietário não encontrado com ID: " + request.ownerId()));
		
		
		Vehicle vehicle = new Vehicle();
		vehicle.setPlate(request.plate());
		vehicle.setBrand(request.brand());
		vehicle.setModel(request.model());
		vehicle.setYear(request.year());
		vehicle.setOwner(owner);
		
		Vehicle savedVehicle = vehicleRepository.save(vehicle);
		
		return new VehicleResponseDto(
				savedVehicle.getId(),
				savedVehicle.getPlate(),
				savedVehicle.getBrand(),
				savedVehicle.getModel(),
				savedVehicle.getYear(),
				savedVehicle.getOwner().getName()
		);
		
	}
	
	@Transactional(readOnly = true)
	public List<VehicleResponseDto> findVehiclesByClientId(Long clientId) {
	    List<Vehicle> vehicles = vehicleRepository.findByOwnerId(clientId);
	    return vehicles.stream()
	            .map(vehicle -> new VehicleResponseDto(
	                    vehicle.getId(),
	                    vehicle.getPlate(),
	                    vehicle.getBrand(),
	                    vehicle.getModel(),
	                    vehicle.getYear(),
	                    vehicle.getOwner().getName()
	            ))
	            .collect(Collectors.toList());
	}
	
	
}

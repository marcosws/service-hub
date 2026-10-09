package com.github.marcosws.servicehub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.marcosws.servicehub.automotive.domain.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	Optional<Vehicle> findByPlate(String plate);
	
	List<Vehicle> findByOwnerId(Long ownerId);

}

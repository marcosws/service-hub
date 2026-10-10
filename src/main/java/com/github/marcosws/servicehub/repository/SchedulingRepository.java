package com.github.marcosws.servicehub.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.marcosws.servicehub.automotive.domain.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
	
	List<Scheduling> findByClientId(Long clientId);
	
	boolean existsByVehicleIdAndScheduledDateTimeAndStatusNot(
			Long vehicleId, 
			LocalDateTime scheduledDateTime,
			Scheduling.SchedulingStatus schedulingStatus
	);
	
	long countByScheduledDateTimeBetweenAndStatusNot(
			LocalDateTime scheduledDateTime,  
			Scheduling.SchedulingStatus schedulingStatus
	);

}

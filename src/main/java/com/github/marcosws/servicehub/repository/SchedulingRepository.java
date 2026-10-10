package com.github.marcosws.servicehub.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.marcosws.servicehub.automotive.domain.Scheduling;

public interface SchedulingRepository extends JpaRepository<Scheduling, Long> {
	
	List<Scheduling> findByClientId(Long clientId);
	
	@Query("SELECT s FROM Scheduling s WHERE s.vehicle.id = :vehicleId AND s.scheduledDateTime = :scheduledDateTime AND s.schedulingStatus <> :schedulingStatus")
	boolean existsByVehicleIdAndScheduledDateTimeAndSchedulingStatusNot(
			@Param("vehicleId") Long vehicleId, 
			@Param("scheduledDateTime") LocalDateTime scheduledDateTime,
			@Param("schedulingStatus") Scheduling.SchedulingStatus schedulingStatus
	);
	
	@Query("SELECT COUNT(s) FROM Scheduling s WHERE s.scheduledDateTime BETWEEN :startDateTime AND :endDateTime AND s.schedulingStatus <> :schedulingStatus")
	long countByScheduledDateTimeBetweenAndSchedulingStatusNot(
			@Param("scheduledDateTime") LocalDateTime scheduledDateTime,  
			@Param("schedulingStatus") Scheduling.SchedulingStatus schedulingStatus
	);

}

package com.github.marcosws.servicehub.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.marcosws.servicehub.automotive.domain.AutomotiveOffering;
import com.github.marcosws.servicehub.automotive.domain.Scheduling;
import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.domain.Vehicle;
import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingRequestDto;
import com.github.marcosws.servicehub.exception.BusinessRulesException;
import com.github.marcosws.servicehub.repository.AutomotiveOfferingRepository;
import com.github.marcosws.servicehub.repository.SchedulingRepository;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class SchedulingServiceTest {
	
	@Mock
	private SchedulingRepository schedulingRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private VehicleRepository vehicleRepository;
	
	@Mock
	private AutomotiveOfferingRepository automotiveOfferingRepository;
	
	@InjectMocks
	private SchedulingService schedulingService;
	
	
	private User client;
	private Vehicle vehicle;
	private AutomotiveOffering service;
	private LocalDateTime validFutureDateTime;
	
	@BeforeEach
	void setUp() {
		
		client = new User();
		client.setId(1L);
		client.setName("John Doe");
		client.setEmail("johndoe@gmail.com");
		client.setRole(User.Role.ROLE_USER);
		
		vehicle = new Vehicle();
		vehicle.setId(10L);
		vehicle.setPlate("ABC-1234");
		vehicle.setModel("Toyota Corolla");
		vehicle.setOwner(client);
		
		service = new AutomotiveOffering();
		service.setId(100L);
		service.setName("Oil Change");
		service.setDurationInMinutes(45);
		service.setBasePrice(new BigDecimal("150.00"));
		
		validFutureDateTime = LocalDateTime.now().plusDays(1);
		while (validFutureDateTime.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || validFutureDateTime.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
			validFutureDateTime = validFutureDateTime.plusDays(1);
		}
		validFutureDateTime = validFutureDateTime.withHour(10).withMinute(0).withSecond(0).withNano(0);
		
	}
	
	@Test
	@DisplayName("Deve criar um agendamento com sucesso quando todas as regras forem atendidas")
	void testCreateSchedulingWithSuccess() {
		
		SchedulingRequestDto request = new SchedulingRequestDto(vehicle.getId(), service.getId(), validFutureDateTime);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(client));
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		when(automotiveOfferingRepository.findById(service.getId())).thenReturn(Optional.of(service));
		when(schedulingRepository.existsByVehicleIdAndScheduledDateTimeAndSchedulingStatusNot(any(), any(), any())).thenReturn(false);
		when(schedulingRepository.countByScheduledDateTimeBetweenAndSchedulingStatusNot(any(), any(), any())).thenReturn(0L);
		
		when(schedulingRepository.save(any(Scheduling.class))).thenAnswer(invocation -> {
			Scheduling scheduling = invocation.getArgument(0);
			scheduling.setId(1L);
			return scheduling;
		});
		
		var response = schedulingService.createScheduling(client.getId(), request);
		
		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("John Doe", response.clientName());
		assertEquals("Oil Change", response.serviceName());
		assertEquals("PENDING", response.status());
		
		verify(schedulingRepository, times(1)).save(any(Scheduling.class));
		
	}
	
	@Test
	@DisplayName("Deve lançar exceção ao tentar agendar para horário fora do comercial")
	void testExceptionWhenOutOfBusinessHours() {
		
		LocalDateTime invalidDateTime = validFutureDateTime.withHour(22); // Fora do horário comercial
		SchedulingRequestDto request = new SchedulingRequestDto(vehicle.getId(), service.getId(), invalidDateTime);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(client));
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		when(automotiveOfferingRepository.findById(service.getId())).thenReturn(Optional.of(service));
		
		BusinessRulesException exception = assertThrows(BusinessRulesException.class, () -> {
			schedulingService.createScheduling(client.getId(), request);
		});
		
		assertEquals("O horário do agendamento deve estar entre 08:00 e 18:00.", exception.getMessage());
		verify(schedulingRepository, times(0)).save(any(Scheduling.class));

	}
	
	@Test
	@DisplayName("Deve lançar exceção quando a oficina atingir a capacidade máxima no horário")
	void testExceptionWhenMaxCapacityExceeded() {
		
		SchedulingRequestDto request = new SchedulingRequestDto(vehicle.getId(), service.getId(), validFutureDateTime);
		
		when(userRepository.findById(1L)).thenReturn(Optional.of(client));
		when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
		when(automotiveOfferingRepository.findById(service.getId())).thenReturn(Optional.of(service));
		when(schedulingRepository.existsByVehicleIdAndScheduledDateTimeAndSchedulingStatusNot(any(), any(), any())).thenReturn(false);
		when(schedulingRepository.countByScheduledDateTimeBetweenAndSchedulingStatusNot(any(), any(), any())).thenReturn(3L); // Capacidade máxima atingida
		
		BusinessRulesException exception = assertThrows(BusinessRulesException.class, () -> {
			schedulingService.createScheduling(client.getId(), request);
		});
		
		assertEquals("A oficina atingiu a capacidade máxima de atendimentos para este horário.", exception.getMessage());
		verify(schedulingRepository, never()).save(any(Scheduling.class));
		
	}
	
	
	

}

package com.github.marcosws.servicehub.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.marcosws.servicehub.automotive.domain.AutomotiveOffering;
import com.github.marcosws.servicehub.automotive.domain.Scheduling;
import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.domain.Vehicle;
import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingRequestDto;
import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingResponseDto;
import com.github.marcosws.servicehub.exception.BusinessRulesException;
import com.github.marcosws.servicehub.repository.AutomotiveOfferingRepository;
import com.github.marcosws.servicehub.repository.SchedulingRepository;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.repository.VehicleRepository;



@Service
public class SchedulingService {
	
	private static final int MAXIMUM_SIMULTANEOUS_CAPACITY = 3;
	private static final int MINIMUM_ADVANCE_NOTICE_FOR_CANCELLATION_HOURS = 2;
	
	private final SchedulingRepository schedulingRepository;
	private final UserRepository userRepository;
	private final VehicleRepository vehicleRepository;
	private final AutomotiveOfferingRepository automotiveOfferingRepository;
	
	public SchedulingService(
			SchedulingRepository schedulingRepository, 
			UserRepository userRepository, 
			VehicleRepository vehicleRepository, 
			AutomotiveOfferingRepository automotiveOfferingRepository) {
		this.schedulingRepository = schedulingRepository;
		this.userRepository = userRepository;
		this.vehicleRepository = vehicleRepository;
		this.automotiveOfferingRepository = automotiveOfferingRepository;
	}
	
	@Transactional
	public SchedulingResponseDto createScheduling(Long clientId, SchedulingRequestDto dto) {

		User client = userRepository.findById(clientId)
				.orElseThrow(() -> new BusinessRulesException("Cliente não encontrado."));
		
		Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
				.orElseThrow(() -> new BusinessRulesException("Veículo não encontrado."));
		
		AutomotiveOffering service = automotiveOfferingRepository.findById(dto.serviceId())
				.orElseThrow(() -> new BusinessRulesException("Serviço automotivo não encontrado."));
		
		if(!vehicle.getOwner().getId().equals(client.getId())) {
			throw new BusinessRulesException("O veículo não pertence ao cliente.");
		}
		
		validateBusinessHours(dto.scheduledDateTime());
		validateExistingAppointmentForVehicle(vehicle.getId(), dto.scheduledDateTime());
		validateWorkshopCapacity(dto.scheduledDateTime());
		
		Scheduling scheduling = new Scheduling(client, vehicle, service, dto.scheduledDateTime());
		scheduling = schedulingRepository.save(scheduling);
		
		return mapperForDto(scheduling); 
	}
	
	
	@Transactional
    public void cancelarAgendamento(Long agendamentoId, Long clienteId) {
        Scheduling scheduling = schedulingRepository.findById(agendamentoId)
                .orElseThrow(() -> new BusinessRulesException("Agendamento não encontrado."));

        if (!scheduling.getClient().getId().equals(clienteId)) {
            throw new BusinessRulesException("Você não tem permissão para cancelar este agendamento.");
        }

        if (scheduling.getScheduledDateTime().isBefore(LocalDateTime.now().plusHours(MINIMUM_ADVANCE_NOTICE_FOR_CANCELLATION_HOURS))) {
            throw new BusinessRulesException(
            		String.format(
            				"Cancelamentos devem ser feitos com no mínimo %d horas de antecedência.",
            				MINIMUM_ADVANCE_NOTICE_FOR_CANCELLATION_HOURS));
        }

        scheduling.setSchedulingStatus(Scheduling.SchedulingStatus.CANCELED);
        schedulingRepository.save(scheduling);
    }

    @Transactional(readOnly = true)
    public List<SchedulingResponseDto> listarAgendamentosDoCliente(Long clientId) {
        return schedulingRepository.findByClientId(clientId)
                .stream()
                .map(this::mapperForDto)
                .toList();
    }
	
	private void validateBusinessHours(LocalDateTime dateTime) {
		if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessRulesException("Não é possível agendar para datas ou horários passados.");
        }

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BusinessRulesException("A oficina não abre aos domingos.");
        }

        LocalTime hora = dateTime.toLocalTime();
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new BusinessRulesException("O horário do agendamento deve estar entre 08:00 e 18:00.");
        }
	}
	
	private void validateExistingAppointmentForVehicle(Long vehicleId, LocalDateTime dateTime) {
		boolean schedulingExists = schedulingRepository.existsByVehicleIdAndScheduledDateTimeAndStatusNot(
                vehicleId,
                dateTime,
                Scheduling.SchedulingStatus.CANCELED
        );

        if (schedulingExists) {
            throw new BusinessRulesException("Este veículo já possui um agendamento ativo para este mesmo horário.");
        }
		
	}
	
	private void validateWorkshopCapacity(LocalDateTime dateTime) {
		long schedulingInHour = schedulingRepository.countByScheduledDateTimeBetweenAndStatusNot(
				dateTime, 
				Scheduling.SchedulingStatus.CANCELED
				);

        if (schedulingInHour >= MAXIMUM_SIMULTANEOUS_CAPACITY) {
            throw new BusinessRulesException("A oficina atingiu a capacidade máxima de atendimentos para este horário.");
        }
		
	}
	
	private SchedulingResponseDto mapperForDto(Scheduling scheduling) {
		return new SchedulingResponseDto(
				scheduling.getId(),
				scheduling.getClient().getName(),
				scheduling.getVehicle().getModel(),
				scheduling.getVehicle().getPlate(),
				scheduling.getService().getName(),
				scheduling.getScheduledDateTime(),
				scheduling.getSchedulingStatus().name()
		);
	}

	
}

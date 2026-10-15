package com.github.marcosws.servicehub.automotive.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class SchedulingDtos {
	
	@Schema(description = "DTO de requisição para criar um novo agendamento de serviço")
	public record SchedulingRequestDto(
			
	        @Schema(description = "ID do veículo que passará pelo serviço", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotNull(message = "O ID do veículo é obrigatório")
			Long vehicleId, 
			
	        @Schema(description = "ID do serviço automotivo escolhido no catálogo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotNull(message = "O ID do serviço é obrigatório")
			Long serviceId, 
			
	        @Schema(description = "Data e hora agendada (deve ser no futuro)", example = "2026-12-20T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotNull(message = "A data e hora são obrigatórias")
	        @Future(message = "A data do agendamento deve ser no futuro")
			LocalDateTime scheduledDateTime
	) {}
	
	@Schema(description = "DTO de resposta contendo os detalhes do agendamento realizado")
	public record SchedulingResponseDto(
			
			@Schema(description = "Identificador único do agendamento", example = "1")
			Long id, 
			
			@Schema(description = "Nome do cliente", example = "John Doe")
			String clientName,
			
			@Schema(description = "Nome do modelo", example = "Corolla")
			String vehicleModel,
			
			@Schema(description = "Placa do veículo", example = "ABC-1234")
			String vehiclePlate,
			
			@Schema(description = "Nome do serviço agendado", example = "Troca de Óleo")
			String serviceName,
			
			@Schema(description = "Data e hora do agendamento", example = "2026-12-20T10:30:00")
			LocalDateTime scheduledDateTime,
			
			@Schema(description = "Status atual do agendamento", example = "PENDING")
			String status
	) {}

}

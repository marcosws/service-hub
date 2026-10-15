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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.github.marcosws.servicehub.automotive.dto.SchedulingDtos.SchedulingRequestDto;
import com.github.marcosws.servicehub.automotive.dto.SchedulingDtos.SchedulingResponseDto;
import com.github.marcosws.servicehub.service.SchedulingService;



@RestController
@RequestMapping("/api/schedulings")
@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento e marcação de serviços da oficina")
public class SchedulingController {
	
	private final SchedulingService schedulingService;
	
	public SchedulingController(SchedulingService schedulingService) {
		this.schedulingService = schedulingService;
	}
	
	@PostMapping
    @Operation(
	    summary = "Criar novo agendamento",
	    description = "Agenda um serviço automotivo para um veículo e cliente específicos, validando disponibilidade. Requer token JWT.",
	    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso", 
	    		content = @Content(schema = @Schema(implementation = SchedulingResponseDto.class))),
	    @ApiResponse(responseCode = "400", description = "Dados inválidos, conflito de horário ou entidades não encontradas"),
	    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
	public ResponseEntity<SchedulingResponseDto> createScheduling(
			@RequestParam Long clientId,
			@Valid @RequestBody SchedulingRequestDto requestDto
			) {
		SchedulingResponseDto response = schedulingService.createScheduling(clientId, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/client/{clientId}")
	@Operation(
		summary = "Listar agendamentos do cliente",
		description = "Retorna todos os agendamentos vinculados a um cliente específico informado na URL. Requer token JWT.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso", 
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = SchedulingResponseDto.class)))),
		@ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido"),
		@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	})
	public ResponseEntity<List<SchedulingResponseDto>> listForClient(@PathVariable Long clientId) {
		List<SchedulingResponseDto> schedulings = schedulingService.listCustomerScheduling(clientId);
		return ResponseEntity.ok(schedulings);

	}
	
	@PatchMapping("{id}/cancel")
	@Operation(
		summary = "Cancelar agendamento",
		description = "Altera o status de um agendamento específico para cancelado. Requer token JWT.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Agendamento cancelado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Requisição inválida ou agendamento não pode ser cancelado"),
		@ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido"),
		@ApiResponse(responseCode = "404", description = "Agendamento ou cliente não encontrado")
	})
	public ResponseEntity<Void> cancelScheduling(@PathVariable Long clientId, @RequestParam Long schedulingId) {
		schedulingService.cancelScheduling(clientId, schedulingId);
		return ResponseEntity.noContent().build();
	}
	

}

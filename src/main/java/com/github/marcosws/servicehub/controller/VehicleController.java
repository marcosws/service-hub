package com.github.marcosws.servicehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleRequestDto;
import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleResponseDto;
import com.github.marcosws.servicehub.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos dos clientes")
public class VehicleController {
	
	private final VehicleService vehicleService;
	
	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	@PostMapping
	@Operation(
			summary = "Cadastrar novo veículo",
			description = "Associa um novo veículo a um cliente existente no sistema. Requer autenticação JWT.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso", 
		       content = @Content(schema = @Schema(implementation = VehicleResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou proprietário não encontrado"),
			@ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
	})
	public ResponseEntity<VehicleResponseDto> registerVehicle(@Valid @RequestBody VehicleRequestDto request){
		VehicleResponseDto response = vehicleService.registerVehicle(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/client/{clientId}")
	@Operation(
			summary = "Listar veículos por cliente",
			description = "Retorna todos os veículos pertencentes a um cliente específico com base no ID do usuário.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso", 
					content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleResponseDto.class)))),
			@ApiResponse(responseCode = "401", description = "Não autorizado")
	})
	public ResponseEntity<List<VehicleResponseDto>> getVehiclesByClientId(@PathVariable Long clientId) {
        List<VehicleResponseDto> vehicles = vehicleService.findVehiclesByClientId(clientId);
        return ResponseEntity.ok(vehicles);
	}
	

}

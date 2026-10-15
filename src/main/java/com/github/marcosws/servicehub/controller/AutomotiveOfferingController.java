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

import com.github.marcosws.servicehub.automotive.dto.AutomotiveOfferingDtos.AutomotiveOfferingRequestDto;
import com.github.marcosws.servicehub.automotive.dto.AutomotiveOfferingDtos.AutomotiveOfferingResponseDto;
import com.github.marcosws.servicehub.service.AutomotiveOfferingService;

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
@RequestMapping("/api/services")
@Tag(name = "Catálogo de Serviços", description = "Endpoints para gerenciamento de serviços automotivos da oficina")
public class AutomotiveOfferingController {
	
	private final AutomotiveOfferingService service;
	
	public AutomotiveOfferingController(AutomotiveOfferingService service) {
		this.service = service;
	}
	
	@PostMapping
    @Operation(
	    summary = "Cadastrar novo serviço automotivo",
	    description = "Cria um novo serviço no catálogo da oficina (ex: troca de óleo, alinhamento). Requer perfil de ADMIN e token JWT válido.",
	    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "201", description = "Serviço cadastrado com sucesso", 
	    		content = @Content(schema = @Schema(implementation = AutomotiveOfferingResponseDto.class))),
	    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos na requisição"),
	    @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
	public ResponseEntity<AutomotiveOfferingResponseDto> register(@Valid @RequestBody AutomotiveOfferingRequestDto request){
        AutomotiveOfferingResponseDto response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping
    @Operation(
	    summary = "Listar todos os serviços automotivos",
	    description = "Retorna uma lista contendo todos os serviços disponíveis no catálogo da oficina."
    )
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso", 
	    		content = @Content(array = @ArraySchema(schema = @Schema(implementation = AutomotiveOfferingResponseDto.class)))),
	    @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
	public ResponseEntity<List<AutomotiveOfferingResponseDto>> findAll() {
        List<AutomotiveOfferingResponseDto> list = service.findAll();
        return ResponseEntity.ok(list);
	}
	
    @GetMapping("/{id}")
    @Operation(
	    summary = "Buscar serviço por ID",
	    description = "Retorna os detalhes de um serviço específico com base no ID informado."
    )
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso", 
	    		content = @Content(schema = @Schema(implementation = AutomotiveOfferingResponseDto.class))),
	    @ApiResponse(responseCode = "404", description = "Serviço não encontrado"),
	    @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<AutomotiveOfferingResponseDto> findById(@PathVariable Long id) {
        AutomotiveOfferingResponseDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }
	

}
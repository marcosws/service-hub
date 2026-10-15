package com.github.marcosws.servicehub.automotive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VehicleDtos {

	@Schema(description = "DTO de requisição para cadastro de um novo veículo")
	public record VehicleRequestDto(
			
			@Schema(description = "Placa do veículo", example = "ABC-1234", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotBlank(message = "A placa é obrigatória")
	        String plate,

	        @Schema(description = "Marca do veículo", example = "Toyota", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotBlank(message = "A marca é obrigatória")
	        String brand,

	        @Schema(description = "Modelo do veículo", example = "Corolla", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotBlank(message = "O modelo é obrigatório")
	        String model,

	        @Schema(description = "Ano de fabricação do veículo", example = "2022", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotNull(message = "O ano é obrigatório")
	        @Positive(message = "O ano deve ser um valor válido")
	        Integer year,

	        @Schema(description = "ID do usuário proprietário do veículo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	        @NotNull(message = "O ID do proprietário (cliente) é obrigatório")
	        Long ownerId
	    ) {}

		@Schema(description = "DTO de resposta contendo os dados do veículo cadastrado")
	    public record VehicleResponseDto(
	    		
	    	@Schema(description = "Identificador único do veículo", example = "1")	
	        Long id,
	        
	        @Schema(description = "Placa do veículo", example = "ABC-1234")
	        String plate,
	        
	        @Schema(description = "Marca do veículo", example = "Toyota")
	        String brand,
	        
	        @Schema(description = "Modelo do veículo", example = "Corolla")
	        String model,
	        
	        @Schema(description = "Ano de fabricação", example = "2022")
	        Integer year,
	        
	        @Schema(description = "Nome do proprietário do veículo", example = "John Doe")
	        String ownerName
	    ) {}
	
}

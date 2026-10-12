package com.github.marcosws.servicehub.automotive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VehicleDtos {

	public record VehicleRequestDto(
	        @NotBlank(message = "A placa é obrigatória")
	        String plate,

	        @NotBlank(message = "A marca é obrigatória")
	        String brand,

	        @NotBlank(message = "O modelo é obrigatório")
	        String model,

	        @NotNull(message = "O ano é obrigatório")
	        @Positive(message = "O ano deve ser um valor válido")
	        Integer year,

	        @NotNull(message = "O ID do proprietário (cliente) é obrigatório")
	        Long ownerId
	    ) {}

	    public record VehicleResponseDto(
	        Long id,
	        String plate,
	        String brand,
	        String model,
	        Integer year,
	        String ownerName
	    ) {}
	
}

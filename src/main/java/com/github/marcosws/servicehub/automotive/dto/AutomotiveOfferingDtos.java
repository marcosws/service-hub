package com.github.marcosws.servicehub.automotive.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AutomotiveOfferingDtos {
	
	public record AutomotiveOfferingRequestDto(
			
			@NotBlank(message = "O nome é obrigatório")
			String name,
			
			String description,
			
			@NotNull(message = "O preço base é obrigatório")
			@Positive(message = "O preço deve ser positivo")
			BigDecimal basePrice,
			
			@NotNull(message = "A duração é obrigatória")
			@Positive(message = "A duração deve ser positiva")
			Integer durationInMinutes
			
	) {}
	
	public record AutomotiveOfferingResponseDto(
			
			Long id,
			String name,
			String description,
			BigDecimal basePrice,
			Integer durationInMinutes
			
	) {}

}

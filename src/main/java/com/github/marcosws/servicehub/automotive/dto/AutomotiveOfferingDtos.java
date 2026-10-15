package com.github.marcosws.servicehub.automotive.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AutomotiveOfferingDtos {
	
	@Schema(description = "DTO de requisição para cadastro de um novo serviço automotivo")
	public record AutomotiveOfferingRequestDto(
			
			@Schema(description = "DTO de requisição para cadastro de um novo serviço automotivo")
			@NotBlank(message = "O nome é obrigatório")
			String name,
			
			@Schema(description = "Detalhes adicionais sobre o que o serviço inclui", example = "Substituição do óleo sintético e do filtro de óleo")
			String description,
			
			@Schema(description = "Preço base cobrado pelo serviço em Reais", example = "150.00", requiredMode = Schema.RequiredMode.REQUIRED)
			@NotNull(message = "O preço base é obrigatório")
			@Positive(message = "O preço deve ser positivo")
			BigDecimal basePrice,
			
			@Schema(description = "Duração estimada para execução do serviço em minutos", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
			@NotNull(message = "A duração é obrigatória")
			@Positive(message = "A duração deve ser positiva")
			Integer durationInMinutes
			
	) {}
	
	@Schema(description = "DTO de resposta contendo os dados do serviço cadastrado")
	public record AutomotiveOfferingResponseDto(
			
			@Schema(description = "Identificador único gerado pelo sistema", example = "1")
			Long id,
			
			@Schema(description = "Nome do serviço", example = "Troca de Óleo do Motor")
			String name,
			
			@Schema(description = "Descrição do serviço", example = "Substituição do óleo sintético e do filtro de óleo")
			String description,
			
			@Schema(description = "Preço base do serviço", example = "150.00")
			BigDecimal basePrice,
			
			@Schema(description = "Duração em minutos", example = "45")
			Integer durationInMinutes
			
	) {}

}

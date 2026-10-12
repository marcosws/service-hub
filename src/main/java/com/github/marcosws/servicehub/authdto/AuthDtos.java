package com.github.marcosws.servicehub.authdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
	public record LoginRequestDTO(
	        @NotBlank(message = "O e-mail é obrigatório")
	        @Email(message = "Formato de e-mail inválido")
	        String email,

	        @NotBlank(message = "A senha é obrigatória")
	        String senha
	    ) {}

	    public record TokenResponseDTO(
	        String token,
	        String tipo
	    ) {}
}

package com.github.marcosws.servicehub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.marcosws.servicehub.authdto.AuthDtos.LoginRequestDTO;
import com.github.marcosws.servicehub.authdto.AuthDtos.TokenResponseDTO;
import com.github.marcosws.servicehub.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> authenticate(@Valid @RequestBody LoginRequestDTO request) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.email(), 
						request.senha()
				)
		);
		
        // Extrai o usuário autenticado do contexto
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Gera o token JWT correspondente
        String jwtToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenResponseDTO(jwtToken, "Bearer"));
	}
	

}

package com.github.marcosws.servicehub.automotive.dto;

public class LoginDtos {
	
	public record LoginRequestDto(
			String email,
			String password
	) {}
	
	public record TokenResponseDto(
			String token,
			String type
	) {}

}

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/services")
public class AutomotiveOfferingController {
	
	private final AutomotiveOfferingService service;
	
	public AutomotiveOfferingController(AutomotiveOfferingService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<AutomotiveOfferingResponseDto> register(@Valid @RequestBody AutomotiveOfferingRequestDto request){
        AutomotiveOfferingResponseDto response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping
	public ResponseEntity<List<AutomotiveOfferingResponseDto>> findAll() {
        List<AutomotiveOfferingResponseDto> list = service.findAll();
        return ResponseEntity.ok(list);
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<AutomotiveOfferingResponseDto> findById(@PathVariable Long id) {
        AutomotiveOfferingResponseDto response = service.findById(id);
        return ResponseEntity.ok(response);
    }
	

}
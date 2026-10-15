package com.github.marcosws.servicehub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.marcosws.servicehub.automotive.domain.AutomotiveOffering;
import com.github.marcosws.servicehub.automotive.dto.AutomotiveOfferingDtos.AutomotiveOfferingRequestDto;
import com.github.marcosws.servicehub.automotive.dto.AutomotiveOfferingDtos.AutomotiveOfferingResponseDto;
import com.github.marcosws.servicehub.exception.BusinessRulesException;
import com.github.marcosws.servicehub.repository.AutomotiveOfferingRepository;


@Service
public class AutomotiveOfferingService {
	
	private final AutomotiveOfferingRepository repository;
	
	public AutomotiveOfferingService(AutomotiveOfferingRepository repository) {
		this.repository = repository;
	}
	
	@Transactional
	public AutomotiveOfferingResponseDto register(AutomotiveOfferingRequestDto request) {
		AutomotiveOffering offering = new AutomotiveOffering();
        offering.setName(request.name());
        offering.setDescription(request.description());
        offering.setBasePrice(request.basePrice());
        offering.setDurationInMinutes(request.durationInMinutes());

        AutomotiveOffering saved = repository.save(offering);

        return toDto(saved);
	}
	
	@Transactional(readOnly = true)
    public List<AutomotiveOfferingResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AutomotiveOfferingResponseDto findById(Long id) {
        AutomotiveOffering offering = repository.findById(id)
                .orElseThrow(() -> new BusinessRulesException("Serviço automotivo não encontrado com ID: " + id));
        return toDto(offering);
    }

    private AutomotiveOfferingResponseDto toDto(AutomotiveOffering offering) {
        return new AutomotiveOfferingResponseDto(
                offering.getId(),
                offering.getName(),
                offering.getDescription(),
                offering.getBasePrice(),
                offering.getDurationInMinutes()
        );
    }

}

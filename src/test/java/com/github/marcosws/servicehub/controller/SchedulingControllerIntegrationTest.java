package com.github.marcosws.servicehub.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.marcosws.servicehub.automotive.domain.AutomotiveOffering;
import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.domain.Vehicle;
import com.github.marcosws.servicehub.automotive.dto.Dtos.SchedulingRequestDto;
import com.github.marcosws.servicehub.repository.AutomotiveOfferingRepository;
import com.github.marcosws.servicehub.repository.SchedulingRepository;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.repository.VehicleRepository;
import com.github.marcosws.servicehub.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class SchedulingControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private AutomotiveOfferingRepository automotiveOfferingRepository;
	
	@Autowired
	private SchedulingRepository schedulingRepository;
	
    @Autowired
    private JwtService jwtService; // Injetamos o JwtService para gerar o token nos testes
	
	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	private User savedUser;
	private Vehicle savedVehicle;
	private AutomotiveOffering savedService;
	private LocalDateTime validDateTime;
    private String tokenJwt;
	
	
	@BeforeEach
	void setUp() {
		// limpar os repositórios antes de cada teste
		schedulingRepository.deleteAll();
		vehicleRepository.deleteAll();
		userRepository.deleteAll();
		automotiveOfferingRepository.deleteAll();
		
		// criar um usuário
		User user = new User();
		user.setName("John Doe");
		user.setEmail("johndoe@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("123456"));
		user.setRole(User.Role.ROLE_USER);
		savedUser = userRepository.save(user);
		
        // Gera o token JWT válido programaticamente para este usuário nos testes
        tokenJwt = jwtService.generateToken(savedUser);
		
		Vehicle vehicle = new Vehicle();
		vehicle.setOwner(savedUser);
		vehicle.setPlate("ABC-1234");
		vehicle.setBrand("Toyota");
		vehicle.setModel("Corolla");
		vehicle.setYear(2020);
		savedVehicle = vehicleRepository.save(vehicle);
		
		AutomotiveOffering service = new AutomotiveOffering();
		service.setName("Oil Change");
		service.setDescription("Change the engine oil");
		service.setBasePrice(new BigDecimal(100.0));
		service.setDurationInMinutes(60);
		savedService = automotiveOfferingRepository.save(service);
		
		validDateTime = LocalDateTime.now().plusDays(1);
		while(validDateTime.getDayOfWeek() == DayOfWeek.SUNDAY || validDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
			validDateTime = validDateTime.plusDays(1);
		}
		validDateTime = validDateTime.withHour(14).withMinute(0).withSecond(0).withNano(0);
		
	}
	
	@Test
	@DisplayName("Deve realizar requisição HTTP POST e criar agendamento com sucesso")
	void shouldCreateSchedulingViaHttpWithSuccessfully() throws Exception {
		
		SchedulingRequestDto requestDto = new SchedulingRequestDto(
				savedVehicle.getId(),
				savedService.getId(),
				validDateTime
		);
		
		mockMvc.perform(post("/api/schedulings")
				.header("Authorization", "Bearer " + tokenJwt) // <--- Envio do token JWT no cabeçalho
				.param("clientId", savedUser.getId().toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.clientId").exists())
		.andExpect(jsonPath("$.clientName").value("John Doe"))
		.andExpect(jsonPath("$.serviceService").value("Oil Change"))
		.andExpect(jsonPath("$.status").value("PENDING"));
				
		
	}
	
	
	
	
	
	

}

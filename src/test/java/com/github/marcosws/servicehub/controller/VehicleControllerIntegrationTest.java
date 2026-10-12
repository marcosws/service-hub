package com.github.marcosws.servicehub.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.domain.Vehicle;
import com.github.marcosws.servicehub.automotive.dto.VehicleDtos.VehicleRequestDto;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.repository.VehicleRepository;
import com.github.marcosws.servicehub.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class VehicleControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private JwtService jwtService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private User savedUser;
	private String jwtToken;
	
	@BeforeEach
	void setUp() {
		
		vehicleRepository.deleteAll();
		userRepository.deleteAll();
		
		User user = new User();
		user.setName("Jhon Doe");
		user.setEmail("jhondoe@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("123456"));
		user.setRole(User.Role.ROLE_USER);
		savedUser = userRepository.save(user);
		
		jwtToken = jwtService.generateToken(savedUser);
	}
	
    @Test
    @DisplayName("Deve cadastrar um veículo com sucesso enviando token JWT válido")
	void shouldCreateVehicleWithSuccessfully()  throws Exception {
		
		VehicleRequestDto vehicleRequest = new VehicleRequestDto(
				"ABC-1234",
				"Toyota",
				"Corolla",
				2020,
				savedUser.getId()
		);
		
		mockMvc.perform(post("/api/vehicles")
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(vehicleRequest)))
	        .andExpect(status().isCreated())
	        .andExpect((ResultMatcher) jsonPath("$.id").exists())
	        .andExpect((ResultMatcher) jsonPath("$.plate").value("ABC-1234"))
	        .andExpect((ResultMatcher) jsonPath("$.brand").value("Toyota"))
	        .andExpect((ResultMatcher) jsonPath("$.model").value("Corolla"))
	        .andExpect((ResultMatcher) jsonPath("$.ownerName").value("Jhon Doe"));
		
	}
    
    void shouldListVehiclesByClientIdWithSuccessfully() throws Exception {
    	
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("XYZ-9876");
        vehicle.setBrand("Chevrolet");
        vehicle.setModel("Onix");
        vehicle.setYear(2023);
        vehicle.setOwner(savedUser);
        vehicleRepository.save(vehicle);
    	
        mockMvc.perform(get("/api/vehicles/client/" + savedUser.getId())
                .header("Authorization", "Bearer " + jwtToken)) // <--- Envio do Token JWT
        .andExpect(status().isOk())
        .andExpect((ResultMatcher) jsonPath("$[0].id").exists())
        .andExpect((ResultMatcher) jsonPath("$[0].plate").value("XYZ-9876"))
        .andExpect((ResultMatcher) jsonPath("$[0].model").value("Onix"));
	}
	
	
	
	
	
	
	
	
	

}

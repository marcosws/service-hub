package com.github.marcosws.servicehub.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

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
import com.github.marcosws.servicehub.automotive.domain.AutomotiveOffering;
import com.github.marcosws.servicehub.automotive.domain.User;
import com.github.marcosws.servicehub.automotive.dto.AutomotiveOfferingDtos.AutomotiveOfferingRequestDto;
import com.github.marcosws.servicehub.repository.AutomotiveOfferingRepository;
import com.github.marcosws.servicehub.repository.UserRepository;
import com.github.marcosws.servicehub.security.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AutomotiveOfferingControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private AutomotiveOfferingRepository offeringRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String jwtToken;
    
    @BeforeEach
    void setUp() {
    	
    	offeringRepository.deleteAll();
    	userRepository.deleteAll();
    	
    	User user = new User();
    	user.setName("Admin Test");
    	user.setEmail("admin@gmail.com");
    	user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setRole(User.Role.ROLE_ADMIN);
        User savedUser = userRepository.save(user);

        jwtToken = jwtService.generateToken(savedUser);
    	
    }
    
    @Test
    @DisplayName("Deve cadastrar um serviço automotivo com sucesso")
    void shouldRegisterOfferingSuccessfully() throws Exception {
        AutomotiveOfferingRequestDto request = new AutomotiveOfferingRequestDto(
                "Troca de Óleo",
                "Substituição de óleo do motor e filtro",
                BigDecimal.valueOf(150.00),
                45
        );

        mockMvc.perform(post("/api/services")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Troca de Óleo"))
            .andExpect(jsonPath("$.basePrice").value(150.00))
            .andExpect(jsonPath("$.durationInMinutes").value(45));
    }

    @Test
    @DisplayName("Deve listar todos os serviços automotivos com sucesso")
    void shouldListOfferingsSuccessfully() throws Exception {
        AutomotiveOffering offering = new AutomotiveOffering();
        offering.setName("Alinhamento e Balanceamento");
        offering.setDescription("Alinhamento de direção e balanceamento de rodas");
        offering.setBasePrice(BigDecimal.valueOf(120.00));
        offering.setDurationInMinutes(60);
        offeringRepository.saveAndFlush(offering);

        mockMvc.perform(get("/api/services")
                .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").value("Alinhamento e Balanceamento"));
    }
	
	
}

package com.petsafe.qr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petsafe.qr.dto.PublicPetResponse;
import com.petsafe.qr.exception.GlobalExceptionHandler;
import com.petsafe.qr.exception.ResourceNotFoundException;
import com.petsafe.qr.security.JwtAuthenticationFilter;
import com.petsafe.qr.security.JwtTokenProvider;
import com.petsafe.qr.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("PublicController Tests")
class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetService petService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private PublicPetResponse publicPetResponse;
    private UUID petId;

    @BeforeEach
    void setUp() {
        petId = UUID.randomUUID();

        publicPetResponse = new PublicPetResponse();
        publicPetResponse.setId(petId);
        publicPetResponse.setName("Rex");
        publicPetResponse.setSpecies("Cachorro");
        publicPetResponse.setBreed("Labrador");
        publicPetResponse.setAge(3);
        publicPetResponse.setColor("Dourado");
        publicPetResponse.setIsMissing(false);
        publicPetResponse.setOwnerName("Test Owner");
        publicPetResponse.setOwnerPhone("11999999999");
    }

    @Test
    @DisplayName("GET /api/public/pet/{uuid} - Should return 200 without authentication")
    void testGetPublicPet_Success() throws Exception {
        // Arrange
        when(petService.getPublicPet(petId)).thenReturn(publicPetResponse);

        // Act & Assert
        mockMvc.perform(get("/api/public/pet/{uuid}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(petId.toString()))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.species").value("Cachorro"))
                .andExpect(jsonPath("$.ownerName").value("Test Owner"))
                .andExpect(jsonPath("$.ownerPhone").value("11999999999"));
    }

    @Test
    @DisplayName("GET /api/public/pet/{uuid} - Should return 404 when pet not found")
    void testGetPublicPet_NotFound() throws Exception {
        // Arrange
        when(petService.getPublicPet(petId))
                .thenThrow(new ResourceNotFoundException("Pet não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/public/pet/{uuid}", petId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pet não encontrado"));
    }

    @Test
    @DisplayName("GET /api/public/pet/{uuid} - Should return pet with missing status")
    void testGetPublicPet_MissingPet() throws Exception {
        // Arrange
        publicPetResponse.setIsMissing(true);
        when(petService.getPublicPet(petId)).thenReturn(publicPetResponse);

        // Act & Assert
        mockMvc.perform(get("/api/public/pet/{uuid}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMissing").value(true));
    }

    @Test
    @DisplayName("GET /api/public/pet/{uuid} - Should return pet with medical info")
    void testGetPublicPet_WithMedicalInfo() throws Exception {
        // Arrange
        publicPetResponse.setMedicalInfo("Vacinado");
        publicPetResponse.setAllergies("Nenhuma");
        publicPetResponse.setMedications("Vermifugo mensal");
        when(petService.getPublicPet(petId)).thenReturn(publicPetResponse);

        // Act & Assert
        mockMvc.perform(get("/api/public/pet/{uuid}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicalInfo").value("Vacinado"))
                .andExpect(jsonPath("$.allergies").value("Nenhuma"))
                .andExpect(jsonPath("$.medications").value("Vermifugo mensal"));
    }
}

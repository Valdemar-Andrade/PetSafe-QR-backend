package com.petsafe.qr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petsafe.qr.dto.PetCreateRequest;
import com.petsafe.qr.dto.PetResponse;
import com.petsafe.qr.dto.PetUpdateRequest;
import com.petsafe.qr.exception.GlobalExceptionHandler;
import com.petsafe.qr.exception.ResourceNotFoundException;
import com.petsafe.qr.exception.UnauthorizedException;
import com.petsafe.qr.security.JwtAuthenticationFilter;
import com.petsafe.qr.security.JwtTokenProvider;
import com.petsafe.qr.security.UserPrincipal;
import com.petsafe.qr.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@DisplayName("PetController Tests")
class PetControllerTest {

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

    private PetCreateRequest createRequest;
    private PetUpdateRequest updateRequest;
    private PetResponse petResponse;
    private UUID userId;
    private UUID petId;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        petId = UUID.randomUUID();

        userPrincipal = new UserPrincipal(
                userId, "Test User", "test@example.com", "password",
                Collections.emptyList()
        );

        // Set authentication context
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        createRequest = new PetCreateRequest();
        createRequest.setName("Rex");
        createRequest.setSpecies("Cachorro");
        createRequest.setBreed("Labrador");
        createRequest.setAge(3);

        updateRequest = new PetUpdateRequest();
        updateRequest.setName("Rex Updated");
        updateRequest.setAge(4);

        petResponse = new PetResponse();
        petResponse.setId(petId);
        petResponse.setName("Rex");
        petResponse.setSpecies("Cachorro");
        petResponse.setBreed("Labrador");
        petResponse.setAge(3);
        petResponse.setOwnerId(userId);
        petResponse.setOwnerName("Test User");
        petResponse.setIsMissing(false);
        petResponse.setCreatedAt(LocalDateTime.now());
        petResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /api/pets - Should return 201 on success with authentication")
    void testCreatePet_Success() throws Exception {
        // Arrange
        when(petService.createPet(any(PetCreateRequest.class), any(UUID.class))).thenReturn(petResponse);

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(petId.toString()))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.species").value("Cachorro"));
    }

    @Test
    @DisplayName("POST /api/pets - Should return 400 for invalid data")
    void testCreatePet_InvalidData() throws Exception {
        // Arrange
        PetCreateRequest invalidRequest = new PetCreateRequest();
        invalidRequest.setName(""); // Required field
        invalidRequest.setSpecies(""); // Required field

        // Act & Assert
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    @DisplayName("GET /api/pets - Should return 200 with list of pets")
    void testGetUserPets_Success() throws Exception {
        // Arrange
        PetResponse petResponse2 = new PetResponse();
        petResponse2.setId(UUID.randomUUID());
        petResponse2.setName("Mia");
        petResponse2.setSpecies("Gato");
        petResponse2.setOwnerId(userId);
        petResponse2.setOwnerName("Test User");
        petResponse2.setIsMissing(false);
        petResponse2.setCreatedAt(LocalDateTime.now());
        petResponse2.setUpdatedAt(LocalDateTime.now());

        List<PetResponse> pets = Arrays.asList(petResponse, petResponse2);
        when(petService.getUserPets(any(UUID.class))).thenReturn(pets);

        // Act & Assert
        mockMvc.perform(get("/api/pets")
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[1].name").value("Mia"));
    }

    @Test
    @DisplayName("GET /api/pets/{id} - Should return 200 on success")
    void testGetPetById_Success() throws Exception {
        // Arrange
        when(petService.getPetById(eq(petId), any(UUID.class))).thenReturn(petResponse);

        // Act & Assert
        mockMvc.perform(get("/api/pets/{id}", petId)
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(petId.toString()))
                .andExpect(jsonPath("$.name").value("Rex"));
    }

    @Test
    @DisplayName("GET /api/pets/{id} - Should return 404 when pet not found")
    void testGetPetById_NotFound() throws Exception {
        // Arrange
        when(petService.getPetById(eq(petId), any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Pet não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/pets/{id}", petId)
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pet não encontrado"));
    }

    @Test
    @DisplayName("PUT /api/pets/{id} - Should return 200 on success")
    void testUpdatePet_Success() throws Exception {
        // Arrange
        PetResponse updatedResponse = new PetResponse();
        updatedResponse.setId(petId);
        updatedResponse.setName("Rex Updated");
        updatedResponse.setSpecies("Cachorro");
        updatedResponse.setAge(4);
        updatedResponse.setOwnerId(userId);
        updatedResponse.setOwnerName("Test User");
        updatedResponse.setIsMissing(false);
        updatedResponse.setCreatedAt(LocalDateTime.now());
        updatedResponse.setUpdatedAt(LocalDateTime.now());

        when(petService.updatePet(eq(petId), any(PetUpdateRequest.class), any(UUID.class)))
                .thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rex Updated"))
                .andExpect(jsonPath("$.age").value(4));
    }

    @Test
    @DisplayName("PUT /api/pets/{id} - Should return 403 when not owner")
    void testUpdatePet_Unauthorized() throws Exception {
        // Arrange
        when(petService.updatePet(eq(petId), any(PetUpdateRequest.class), any(UUID.class)))
                .thenThrow(new UnauthorizedException("Você não tem permissão para editar este pet"));

        // Act & Assert
        mockMvc.perform(put("/api/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Você não tem permissão para editar este pet"));
    }

    @Test
    @DisplayName("DELETE /api/pets/{id} - Should return 204 on success")
    void testDeletePet_Success() throws Exception {
        // Arrange
        doNothing().when(petService).deletePet(eq(petId), any(UUID.class));

        // Act & Assert
        mockMvc.perform(delete("/api/pets/{id}", petId)
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/pets/{id}/missing - Should return 200 on success")
    void testToggleMissing_Success() throws Exception {
        // Arrange
        PetResponse toggledResponse = new PetResponse();
        toggledResponse.setId(petId);
        toggledResponse.setName("Rex");
        toggledResponse.setSpecies("Cachorro");
        toggledResponse.setOwnerId(userId);
        toggledResponse.setOwnerName("Test User");
        toggledResponse.setIsMissing(true);
        toggledResponse.setCreatedAt(LocalDateTime.now());
        toggledResponse.setUpdatedAt(LocalDateTime.now());

        when(petService.toggleMissing(eq(petId), any(UUID.class))).thenReturn(toggledResponse);

        // Act & Assert
        mockMvc.perform(patch("/api/pets/{id}/missing", petId)
                        .principal(new UsernamePasswordAuthenticationToken(userPrincipal, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isMissing").value(true));
    }
}

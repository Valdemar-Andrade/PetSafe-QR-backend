package com.petsafe.qr.service;

import com.petsafe.qr.dto.PetCreateRequest;
import com.petsafe.qr.dto.PetResponse;
import com.petsafe.qr.dto.PetUpdateRequest;
import com.petsafe.qr.dto.PublicPetResponse;
import com.petsafe.qr.entity.Pet;
import com.petsafe.qr.entity.User;
import com.petsafe.qr.exception.ResourceNotFoundException;
import com.petsafe.qr.exception.UnauthorizedException;
import com.petsafe.qr.repository.PetRepository;
import com.petsafe.qr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PetService Tests")
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    private User owner;
    private User otherUser;
    private Pet pet;
    private PetCreateRequest createRequest;
    private PetUpdateRequest updateRequest;
    private UUID ownerId;
    private UUID otherUserId;
    private UUID petId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
        petId = UUID.randomUUID();

        owner = new User();
        owner.setId(ownerId);
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner.setPhone("11999999999");

        otherUser = new User();
        otherUser.setId(otherUserId);
        otherUser.setName("Other User");

        pet = new Pet();
        pet.setId(petId);
        pet.setName("Rex");
        pet.setSpecies("Cachorro");
        pet.setBreed("Labrador");
        pet.setAge(3);
        pet.setColor("Dourado");
        pet.setWeight(25.0);
        pet.setOwner(owner);
        pet.setIsMissing(false);
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());

        createRequest = new PetCreateRequest();
        createRequest.setName("Rex");
        createRequest.setSpecies("Cachorro");
        createRequest.setBreed("Labrador");
        createRequest.setAge(3);

        updateRequest = new PetUpdateRequest();
        updateRequest.setName("Rex Updated");
        updateRequest.setAge(4);
    }

    @Test
    @DisplayName("Should create pet successfully with UUID generated")
    void testCreatePet_Success() {
        // Arrange
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // Act
        PetResponse response = petService.createPet(createRequest, ownerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(petId);
        assertThat(response.getName()).isEqualTo("Rex");
        assertThat(response.getSpecies()).isEqualTo("Cachorro");
        assertThat(response.getOwnerId()).isEqualTo(ownerId);

        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("Should return list of user pets")
    void testGetUserPets_Success() {
        // Arrange
        Pet pet2 = new Pet();
        pet2.setId(UUID.randomUUID());
        pet2.setName("Mia");
        pet2.setSpecies("Gato");
        pet2.setOwner(owner);
        pet2.setIsMissing(false);
        pet2.setCreatedAt(LocalDateTime.now());
        pet2.setUpdatedAt(LocalDateTime.now());

        when(petRepository.findByOwnerId(ownerId)).thenReturn(Arrays.asList(pet, pet2));

        // Act
        List<PetResponse> pets = petService.getUserPets(ownerId);

        // Assert
        assertThat(pets).hasSize(2);
        assertThat(pets.get(0).getName()).isEqualTo("Rex");
        assertThat(pets.get(1).getName()).isEqualTo("Mia");
    }

    @Test
    @DisplayName("Should return pet when user is owner")
    void testGetPetById_Success() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act
        PetResponse response = petService.getPetById(petId, ownerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(petId);
        assertThat(response.getName()).isEqualTo("Rex");
    }

    @Test
    @DisplayName("Should throw exception when user is not owner")
    void testGetPetById_Unauthorized() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act & Assert
        assertThatThrownBy(() -> petService.getPetById(petId, otherUserId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Você não tem permissão para ver este pet");
    }

    @Test
    @DisplayName("Should throw exception when pet not found")
    void testGetPetById_NotFound() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> petService.getPetById(petId, ownerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Pet não encontrado");
    }

    @Test
    @DisplayName("Should update pet successfully")
    void testUpdatePet_Success() {
        // Arrange
        Pet updatedPet = new Pet();
        updatedPet.setId(petId);
        updatedPet.setName("Rex Updated");
        updatedPet.setSpecies("Cachorro");
        updatedPet.setAge(4);
        updatedPet.setOwner(owner);
        updatedPet.setIsMissing(false);
        updatedPet.setCreatedAt(LocalDateTime.now());
        updatedPet.setUpdatedAt(LocalDateTime.now());

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        // Act
        PetResponse response = petService.updatePet(petId, updateRequest, ownerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Rex Updated");
        assertThat(response.getAge()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should throw exception when updating pet without authorization")
    void testUpdatePet_Unauthorized() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act & Assert
        assertThatThrownBy(() -> petService.updatePet(petId, updateRequest, otherUserId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Você não tem permissão para editar este pet");
    }

    @Test
    @DisplayName("Should delete pet successfully")
    void testDeletePet_Success() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        doNothing().when(petRepository).delete(pet);

        // Act
        petService.deletePet(petId, ownerId);

        // Assert
        verify(petRepository).delete(pet);
    }

    @Test
    @DisplayName("Should throw exception when deleting pet without authorization")
    void testDeletePet_Unauthorized() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act & Assert
        assertThatThrownBy(() -> petService.deletePet(petId, otherUserId))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Você não tem permissão para deletar este pet");

        verify(petRepository, never()).delete(any(Pet.class));
    }

    @Test
    @DisplayName("Should toggle missing status correctly")
    void testToggleMissing_Success() {
        // Arrange
        Pet toggledPet = new Pet();
        toggledPet.setId(petId);
        toggledPet.setName("Rex");
        toggledPet.setSpecies("Cachorro");
        toggledPet.setOwner(owner);
        toggledPet.setIsMissing(true);
        toggledPet.setCreatedAt(LocalDateTime.now());
        toggledPet.setUpdatedAt(LocalDateTime.now());

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(toggledPet);

        // Act
        PetResponse response = petService.toggleMissing(petId, ownerId);

        // Assert
        assertThat(response.getIsMissing()).isTrue();
    }

    @Test
    @DisplayName("Should return public pet data")
    void testGetPublicPet_Success() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        // Act
        PublicPetResponse response = petService.getPublicPet(petId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(petId);
        assertThat(response.getName()).isEqualTo("Rex");
        assertThat(response.getOwnerName()).isEqualTo("Owner");
    }

    @Test
    @DisplayName("Should throw exception when public pet not found")
    void testGetPublicPet_NotFound() {
        // Arrange
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> petService.getPublicPet(petId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Pet não encontrado");
    }
}

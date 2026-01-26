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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/pets/";
    
    @Transactional
    public PetResponse createPet(PetCreateRequest request, UUID userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        
        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setColor(request.getColor());
        pet.setWeight(request.getWeight());
        pet.setMedicalInfo(request.getMedicalInfo());
        pet.setAllergies(request.getAllergies());
        pet.setMedications(request.getMedications());
        pet.setVetContact(request.getVetContact());
        pet.setOwnerNotes(request.getOwnerNotes());
        pet.setOwner(owner);
        pet.setIsMissing(false);
        
        Pet savedPet = petRepository.save(pet);
        
        return PetResponse.fromEntity(savedPet);
    }
    
    @Transactional(readOnly = true)
    public List<PetResponse> getUserPets(UUID userId) {
        return petRepository.findByOwnerId(userId).stream()
                .map(PetResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PetResponse getPetById(UUID petId, UUID userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para ver este pet");
        }
        
        return PetResponse.fromEntity(pet);
    }
    
    @Transactional(readOnly = true)
    public PublicPetResponse getPublicPet(UUID petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        return PublicPetResponse.fromEntity(pet);
    }
    
    @Transactional
    public PetResponse updatePet(UUID petId, PetUpdateRequest request, UUID userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para editar este pet");
        }
        
        if (request.getName() != null) pet.setName(request.getName());
        if (request.getSpecies() != null) pet.setSpecies(request.getSpecies());
        if (request.getBreed() != null) pet.setBreed(request.getBreed());
        if (request.getAge() != null) pet.setAge(request.getAge());
        if (request.getColor() != null) pet.setColor(request.getColor());
        if (request.getWeight() != null) pet.setWeight(request.getWeight());
        if (request.getMedicalInfo() != null) pet.setMedicalInfo(request.getMedicalInfo());
        if (request.getAllergies() != null) pet.setAllergies(request.getAllergies());
        if (request.getMedications() != null) pet.setMedications(request.getMedications());
        if (request.getVetContact() != null) pet.setVetContact(request.getVetContact());
        if (request.getOwnerNotes() != null) pet.setOwnerNotes(request.getOwnerNotes());
        
        Pet updatedPet = petRepository.save(pet);
        return PetResponse.fromEntity(updatedPet);
    }
    
    @Transactional
    public void deletePet(UUID petId, UUID userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para deletar este pet");
        }
        
        petRepository.delete(pet);
    }
    
    @Transactional
    public PetResponse toggleMissing(UUID petId, UUID userId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para modificar este pet");
        }
        
        pet.setIsMissing(!pet.getIsMissing());
        Pet updatedPet = petRepository.save(pet);
        
        return PetResponse.fromEntity(updatedPet);
    }
    
    @Transactional
    public PetResponse uploadPhoto(UUID petId, MultipartFile file, UUID userId) throws IOException {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para modificar este pet");
        }
        
        // Criar diretório se não existir
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = petId + "_" + System.currentTimeMillis() + extension;
        
        // Salvar arquivo
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Atualizar URL da foto
        pet.setPhotoUrl("/uploads/pets/" + filename);
        Pet updatedPet = petRepository.save(pet);
        
        return PetResponse.fromEntity(updatedPet);
    }
}
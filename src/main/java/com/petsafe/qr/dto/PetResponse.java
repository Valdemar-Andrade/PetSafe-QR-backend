package com.petsafe.qr.dto;

import com.petsafe.qr.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private UUID id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String color;
    private Double weight;
    private String medicalInfo;
    private String allergies;
    private String medications;
    private String vetContact;
    private String ownerNotes;
    private String photoUrl;
    private String qrCodeUrl;
    private Boolean isMissing;
    private UUID ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static PetResponse fromEntity(Pet pet) {
        PetResponse response = new PetResponse();
        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setSpecies(pet.getSpecies());
        response.setBreed(pet.getBreed());
        response.setAge(pet.getAge());
        response.setColor(pet.getColor());
        response.setWeight(pet.getWeight());
        response.setMedicalInfo(pet.getMedicalInfo());
        response.setAllergies(pet.getAllergies());
        response.setMedications(pet.getMedications());
        response.setVetContact(pet.getVetContact());
        response.setOwnerNotes(pet.getOwnerNotes());
        response.setPhotoUrl(pet.getPhotoUrl());
        response.setQrCodeUrl(pet.getQrCodeUrl());
        response.setIsMissing(pet.getIsMissing());
        response.setOwnerId(pet.getOwner().getId());
        response.setOwnerName(pet.getOwner().getName());
        response.setCreatedAt(pet.getCreatedAt());
        response.setUpdatedAt(pet.getUpdatedAt());
        return response;
    }
}
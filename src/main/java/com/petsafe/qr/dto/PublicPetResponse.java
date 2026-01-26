package com.petsafe.qr.dto;

import com.petsafe.qr.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicPetResponse {
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
    private Boolean isMissing;
    private String ownerName;
    private String ownerPhone;
    
    public static PublicPetResponse fromEntity(Pet pet) {
        PublicPetResponse response = new PublicPetResponse();
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
        response.setIsMissing(pet.getIsMissing());
        response.setOwnerName(pet.getOwner().getName());
        response.setOwnerPhone(pet.getOwner().getPhone());
        return response;
    }
}
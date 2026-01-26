package com.petsafe.qr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateRequest {
    @NotBlank(message = "Nome do pet é obrigatório")
    private String name;
    
    @NotBlank(message = "Espécie é obrigatória")
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
}
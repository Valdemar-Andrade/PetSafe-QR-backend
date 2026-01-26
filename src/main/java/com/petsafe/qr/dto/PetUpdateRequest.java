package com.petsafe.qr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdateRequest {
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
}
package com.petsafe.qr.controller;

import com.petsafe.qr.dto.PublicPetResponse;
import com.petsafe.qr.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    
    private final PetService petService;
    
    @GetMapping("/pet/{uuid}")
    public ResponseEntity<PublicPetResponse> getPublicPet(@PathVariable UUID uuid) {
        PublicPetResponse pet = petService.getPublicPet(uuid);
        return ResponseEntity.ok(pet);
    }
}
package com.petsafe.qr.controller;

import com.petsafe.qr.dto.PetCreateRequest;
import com.petsafe.qr.dto.PetResponse;
import com.petsafe.qr.dto.PetUpdateRequest;
import com.petsafe.qr.security.UserPrincipal;
import com.petsafe.qr.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    
    private final PetService petService;
    
    @PostMapping
    public ResponseEntity<PetResponse> createPet(
            @Valid @RequestBody PetCreateRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PetResponse response = petService.createPet(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PetResponse>> getUserPets(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<PetResponse> pets = petService.getUserPets(currentUser.getId());
        return ResponseEntity.ok(pets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PetResponse pet = petService.getPetById(id, currentUser.getId());
        return ResponseEntity.ok(pet);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable UUID id,
            @Valid @RequestBody PetUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PetResponse response = petService.updatePet(id, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        petService.deletePet(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/missing")
    public ResponseEntity<PetResponse> toggleMissing(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PetResponse response = petService.toggleMissing(id, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> uploadPhoto(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal currentUser) throws IOException {
        PetResponse response = petService.uploadPhoto(id, file, currentUser.getId());
        return ResponseEntity.ok(response);
    }
}
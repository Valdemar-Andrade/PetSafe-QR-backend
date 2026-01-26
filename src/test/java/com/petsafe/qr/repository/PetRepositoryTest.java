package com.petsafe.qr.repository;

import com.petsafe.qr.entity.Pet;
import com.petsafe.qr.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("PetRepository Tests")
class PetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    private User owner;
    private User otherOwner;
    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner.setPassword("encodedPassword123");
        owner.setPhone("11999999999");
        entityManager.persist(owner);

        otherOwner = new User();
        otherOwner.setName("Other Owner");
        otherOwner.setEmail("other@example.com");
        otherOwner.setPassword("encodedPassword123");
        otherOwner.setPhone("11888888888");
        entityManager.persist(otherOwner);

        pet1 = new Pet();
        pet1.setName("Rex");
        pet1.setSpecies("Cachorro");
        pet1.setBreed("Labrador");
        pet1.setAge(3);
        pet1.setOwner(owner);
        pet1.setIsMissing(false);

        pet2 = new Pet();
        pet2.setName("Mia");
        pet2.setSpecies("Gato");
        pet2.setBreed("Siames");
        pet2.setAge(2);
        pet2.setOwner(owner);
        pet2.setIsMissing(false);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should find all pets by owner ID")
    void testFindByOwnerId_Success() {
        // Arrange
        entityManager.persist(pet1);
        entityManager.persist(pet2);
        entityManager.flush();

        // Act
        List<Pet> pets = petRepository.findByOwnerId(owner.getId());

        // Assert
        assertThat(pets).hasSize(2);
        assertThat(pets).extracting(Pet::getName).containsExactlyInAnyOrder("Rex", "Mia");
    }

    @Test
    @DisplayName("Should return empty list when owner has no pets")
    void testFindByOwnerId_NoPets() {
        // Act
        List<Pet> pets = petRepository.findByOwnerId(otherOwner.getId());

        // Assert
        assertThat(pets).isEmpty();
    }

    @Test
    @DisplayName("Should find pet by ID successfully")
    void testFindById_Success() {
        // Arrange
        entityManager.persist(pet1);
        entityManager.flush();

        // Act
        Optional<Pet> found = petRepository.findById(pet1.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rex");
        assertThat(found.get().getSpecies()).isEqualTo("Cachorro");
        assertThat(found.get().getOwner().getId()).isEqualTo(owner.getId());
    }

    @Test
    @DisplayName("Should return empty when pet not found")
    void testFindById_NotFound() {
        // Act
        Optional<Pet> found = petRepository.findById(UUID.randomUUID());

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should save and retrieve pet with all fields")
    void testSaveAndRetrieve() {
        // Arrange
        pet1.setColor("Dourado");
        pet1.setWeight(25.0);
        pet1.setMedicalInfo("Vacinado");
        pet1.setAllergies("Nenhuma");
        pet1.setMedications("Vermifugo mensal");
        pet1.setVetContact("11912345678");
        pet1.setOwnerNotes("Dócil e brincalhão");

        // Act
        Pet saved = petRepository.save(pet1);
        entityManager.flush();
        entityManager.clear();

        Optional<Pet> retrieved = petRepository.findById(saved.getId());

        // Assert
        assertThat(retrieved).isPresent();
        Pet pet = retrieved.get();
        assertThat(pet.getName()).isEqualTo("Rex");
        assertThat(pet.getColor()).isEqualTo("Dourado");
        assertThat(pet.getWeight()).isEqualTo(25.0);
        assertThat(pet.getMedicalInfo()).isEqualTo("Vacinado");
        assertThat(pet.getAllergies()).isEqualTo("Nenhuma");
        assertThat(pet.getMedications()).isEqualTo("Vermifugo mensal");
        assertThat(pet.getVetContact()).isEqualTo("11912345678");
        assertThat(pet.getOwnerNotes()).isEqualTo("Dócil e brincalhão");
    }

    @Test
    @DisplayName("Should delete pet successfully")
    void testDelete() {
        // Arrange
        entityManager.persist(pet1);
        entityManager.flush();
        UUID petId = pet1.getId();

        // Act
        petRepository.delete(pet1);
        entityManager.flush();

        Optional<Pet> found = petRepository.findById(petId);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should update pet successfully")
    void testUpdate() {
        // Arrange
        entityManager.persist(pet1);
        entityManager.flush();

        // Act
        pet1.setName("Rex Updated");
        pet1.setAge(4);
        pet1.setIsMissing(true);
        petRepository.save(pet1);
        entityManager.flush();
        entityManager.clear();

        Optional<Pet> updated = petRepository.findById(pet1.getId());

        // Assert
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Rex Updated");
        assertThat(updated.get().getAge()).isEqualTo(4);
        assertThat(updated.get().getIsMissing()).isTrue();
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.DatabaseStuff.PetData.IPetDAO;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetManagerTest {

    private PetManager petManager;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pets = new ArrayList<>();
        pets.add(new Pet("Buddy", "Dog", 3));
        pets.add(new Pet("Mittens", "Cat", 2));
        pets.add(new Pet("Goldie", "Fish", 1));

        IPetDAO petDAO = new IPetDAO() {
            @Override
            public List<Pet> getAllPets() {
                return pets;
            }

            @Override
            public void addPet(Pet pet, int userId) {
                pets.add(pet);
            }

            @Override
            public void deletePet(Pet pet, int userId) {
                pets.remove(pet);
            }

            @Override
            public Pet getPet(int userId, int petId) {
                return null;
            }

            @Override
            public void updatePet(Pet pet, int userId) {
                // For simplicity, assume pet names are unique and update based on name
                for (int i = 0; i < pets.size(); i++) {
                    if (pets.get(i).GetName().equals(pet.GetName())) {
                        pets.set(i, pet);
                        break;
                    }
                }
            }

            @Override
            public List<Pet> getAllUsersPets(int userId) {
                return pets; // Simplified for testing
            }
        };

        petManager = new PetManager(petDAO);
    }

    @Test
    void testSearchPets() {
        List<Pet> result = petManager.searchPets("Buddy");
        assertEquals(1, result.size());
        assertEquals("Buddy", result.get(0).GetName());

        result = petManager.searchPets("Cat");
        assertEquals(1, result.size());
        assertEquals("Mittens", result.get(0).GetName());

        result = petManager.searchPets("Gold");
        assertEquals(1, result.size());
        assertEquals("Goldie", result.get(0).GetName());

        result = petManager.searchPets("Dog");
        assertEquals(1, result.size());
        assertEquals("Buddy", result.get(0).GetName());
    }

    @Test
    void testAddPet() {
        Pet newPet = new Pet("Charlie", "Dog", 4);
        petManager.addPet(newPet, 1);
        assertEquals(4, pets.size());
        assertTrue(pets.contains(newPet));
    }

    @Test
    void testDeletePet() {
        Pet petToDelete = pets.get(0);
        petManager.deletePet(petToDelete, 1);
        assertEquals(2, pets.size());
        assertFalse(pets.contains(petToDelete));
    }

    @Test
    void testUpdatePet() {
        Pet updatedPet = new Pet("Buddy", "Dog", 4);
        petManager.updatePet(updatedPet, 1);
        assertEquals(3, pets.size());
        assertEquals("Buddy", pets.get(0).GetName());
        assertEquals("Dog", pets.get(0).GetType());
        assertEquals(4, pets.get(0).GetAge());
    }

    @Test
    void testGetAllUsersPets() {
        List<Pet> result = petManager.getAllUsersPets(1);
        assertEquals(3, result.size());
    }

    @Test
    void testGetAllPets() {
        List<Pet> result = petManager.getAllPets();
        assertEquals(3, result.size());
    }
}

package vpm.gui_prototype.models.DatabaseStuff;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.List;

/**
 * Interface for the Pet Data Access Object that handles
 * the CRUD operations for the Pet class with the pet database.
 */
public interface IPetDAO {

    /**
     * Adds a new pet of the user's pets to the pet database.
     * @param pet The pet to add.
     * @param userId The user's account id.
     */
    public void addPet(Pet pet, int userId);

    /**
     * Updates an existing pet of the user pets in the pet database.
     * @param pet The pet to update.
     * @param userId The user's account id.
     */
    public void updatePet(Pet pet, int userId);

    /**
     * Deletes a pet of the user pets from the pet database.
     * @param pet The pet to delete.
     * @param userId The user's account id.
     */
    public void deletePet(Pet pet, int userId);

    /**
     * Retrieves a pet of the user pets from the pet database.
     * @param userId The user's account id.
     * @param petId The id of the pet to retrieve.
     * @return The pet with the given id, or null if not found.
     */
    public Pet getPet(int userId, int petId);

    /**
     * Retrieves all users pets from the database.
     * @param userId The user's account id
     * @return A list of all contacts in the database.
     */
    public List<Pet> getAllPets(int userId);
}
package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for the Pet Data Access Object (DAO).
 * This interface defines the CRUD operations for managing
 * pet data in the database.
 */
public interface IPetDAO {

    /**
     * Adds a new pet to the database associated with a specific user.
     *
     * @param pet The pet to be added. Must not be null.
     * @param userId The ID of the user to whom the pet belongs. Must be a valid user ID.
     */
    void addPet(Pet pet, int userId);

    /**
     * Updates an existing pet's details in the database.
     *
     * @param pet The pet with updated details. Must not be null.
     * @param userId The ID of the user who owns the pet. Must be a valid user ID.
     */
    void updatePet(Pet pet, int userId);

    /**
     * Deletes a pet from the database.
     *
     * @param pet The pet to be deleted. Must not be null.
     * @param userId The ID of the user who owns the pet. Must be a valid user ID.
     */
    void deletePet(Pet pet, int userId);

    /**
     * Retrieves a specific pet from the database based on the user ID and pet ID.
     *
     * @param userId The ID of the user who owns the pet. Must be a valid user ID.
     * @param petId The ID of the pet to retrieve. Must be a valid pet ID.
     * @return The pet associated with the given user ID and pet ID, or null if not found.
     */
    Pet getPet(int userId, int petId);

    /**
     * Retrieves all pets associated with a specific user from the database.
     *
     * @param userId The ID of the user whose pets are to be retrieved. Must be a valid user ID.
     * @return A list of pets owned by the specified user. If the user has no pets, returns an empty list.
     */
    List<Pet> getAllUsersPets(int userId);

    /**
     * Retrieves all pets from the database, regardless of ownership.
     *
     * @return A list of all pets in the database. If there are no pets, returns an empty list.
     */
    List<Pet> getAllPets();

    /**
     * Retrieves the last interaction time for a specific pet.
     *
     * @param petId The ID of the pet whose last interaction time is to be retrieved.
     * @return The last interaction time for the pet, or null if not found.
     */
    LocalDateTime getLastInteractionTime(int petId);

    /**
     * Sets the last interaction time for a specific pet.
     *
     * @param petId The ID of the pet for which the last interaction time is to be set.
     */
    void setLastInteractionTime(int petId);
}

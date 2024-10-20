package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.services.TimelineService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Manages pet-related operations, including adding, updating, deleting,
 * and searching for pets in the database.
 */
public class PetManager {
    private final IPetDAO petDAO;

    /**
     * Constructs a PetManager with the specified Pet Data Access Object.
     *
     * @param petDAO The data access object for managing pet data. Must not be null.
     */
    public PetManager(IPetDAO petDAO) {
        this.petDAO = petDAO;
        TimelineService.getInstance().initialize(this);  // Initialize the timeline service
    }

    /**
     * Initializes the timers for all pets after login.
     */
    public void initializePetTimers() {
        List<Pet> allPets = petDAO.getAllPets();
        for (Pet pet : allPets) {
            TimelineService.getInstance().startPetTimers(pet);
        }
    }

    /**
     * Adjusts the pet stats based on time since the last login.
     *
     * @param userId The user's ID.
     */
    public void adjustStatsAfterLogin(int userId) {
        LocalDateTime lastInteractionTime = petDAO.getLastInteractionTime(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        if (lastInteractionTime != null) {
            long secondsPassed = ChronoUnit.SECONDS.between(lastInteractionTime, currentTime);
            List<Pet> userPets = petDAO.getAllUsersPets(userId);
            for (Pet pet : userPets) {
                adjustPetStatsBasedOnTime(pet, secondsPassed);
                TimelineService.getInstance().startPetTimers(pet);  // Start timers for each pet
            }
        }
    }

    private void adjustPetStatsBasedOnTime(Pet pet, long secondsPassed) {
        long happinessDecrements = secondsPassed / pet.getHappinessDecrementInterval();
        long hungerDecrements = secondsPassed / pet.getHungerDecrementInterval();

        pet.decreaseHappiness(0.1f * happinessDecrements);
        pet.feed(-0.1f * hungerDecrements);
        petDAO.updatePet(pet, pet.getUserID());
    }

    /**
     * Stops all timers and saves the logout time.
     *
     * @param userId The user's ID.
     */
    public void stopAllTimersOnLogout(int userId) {
        LocalDateTime logoutTime = LocalDateTime.now();
        petDAO.setLastInteractionTime(userId, logoutTime);
        TimelineService.getInstance().stopAllTimers();  // Stop all timers
    }

    /**
     * Searches for pets that match the given query across their attributes.
     *
     * @param query The search query to match against pet attributes. If null or empty, returns all pets.
     * @return A list of pets that match the search criteria.
     */
    public List<Pet> searchPets(String query) {
        return petDAO.getAllPets()
                .stream()
                .filter(pet -> isPetMatched(pet, query))
                .toList();
    }

    /**
     * Checks if a pet matches the given query based on its attributes.
     *
     * @param pet The pet to check against the query. Must not be null.
     * @param query The search query to match. If null or empty, always returns true.
     * @return true if the pet matches the query; false otherwise.
     */
    private boolean isPetMatched(Pet pet, String query) {
        if (query == null || query.isEmpty()) return true;

        query = query.toLowerCase();
        String searchString = pet.getName() + " " +
                pet.getAge() + " " +
                pet.getType() + " " +
                pet.getColour();

        return searchString.toLowerCase().contains(query);
    }

    /**
     * Adds a new pet to the database.
     *
     * @param pet The pet to add. Must not be null.
     * @param userId The ID of the user adding the pet. Must be a valid user ID.
     */
    public void addPet(Pet pet, int userId) {
        petDAO.addPet(pet, userId);
    }

    /**
     * Deletes a pet from the database.
     *
     * @param pet The pet to delete. Must not be null.
     * @param userId The ID of the user who owns the pet. Must be a valid user ID.
     */
    public void deletePet(Pet pet, int userId) {
        petDAO.deletePet(pet, userId);
    }

    /**
     * Updates an existing pet in the database.
     *
     * @param pet The pet with updated details. Must not be null.
     * @param userId The ID of the user who owns the pet. Must be a valid user ID.
     */
    public void updatePet(Pet pet, int userId) {
        petDAO.updatePet(pet, userId);
    }

    /**
     * Retrieves all pets owned by a specific user from the database.
     *
     * @param userId The ID of the user whose pets are to be retrieved. Must be a valid user ID.
     * @return A list of pets owned by the specified user. If the user has no pets, returns an empty list.
     */
    public List<Pet> getAllUsersPets(int userId) {
        return petDAO.getAllUsersPets(userId);
    }

    /**
     * Retrieves all pets from the database, regardless of ownership.
     *
     * @return A list of all pets in the database. If there are no pets, returns an empty list.
     */
    public List<Pet> getAllPets() {
        return petDAO.getAllPets();
    }
}

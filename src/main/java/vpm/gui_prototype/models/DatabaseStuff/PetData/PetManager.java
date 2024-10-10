package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.List;

/**
 * A class that manages and facilitates a more advanced search functionality
 */
public class PetManager {
    private IPetDAO petDAO;
    //private int userId = 1;
    public PetManager(IPetDAO petDAO) {
        this.petDAO = petDAO;
    }

    /**
     * A method that searches a list of pets and finds the entered SQL query.
     * @param query The SQL query that searches for an entry.
     * @return
     */
    public List<Pet> searchPets(String query) {
        return petDAO.getAllPets()
                .stream()
                .filter(pet -> isPetMatched(pet, query))
                .toList();
    }

    private boolean isPetMatched(Pet pet, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = pet.getName()
                + " " + pet.getAge()
                + " " + pet.getType()
                + " " + pet.getColour();
        return searchString.toLowerCase().contains(query);
    }

    /**
     * Basic method to add a pet to the database
     * @param pet The pet class object
     * @param userId The user's id
     */
    public void addPet(Pet pet, int userId) {
        petDAO.addPet(pet, userId);
    }

    /**
     * Basic method to delete a pet from the database
     * @param pet The contact class object
     * @param userId The user's id
     */
    public void deletePet(Pet pet, int userId) {
        petDAO.deletePet(pet, userId);
    }

    /**
     * Basic method to update a pet in the database
     * @param pet The contact class object
     * @param userId The user's id
     */
    public void updatePet(Pet pet, int userId) {
        petDAO.updatePet(pet, userId);
    }

    /**
     * Basic method to get all user's pets from the database
     */
    public List<Pet> getAllUsersPets(int userId) {
        return petDAO.getAllUsersPets(userId);
    }

    /**
     * Basic method to get all pets from the database
     */
    public List<Pet> getAllPets() {
        return petDAO.getAllPets();
    }
}
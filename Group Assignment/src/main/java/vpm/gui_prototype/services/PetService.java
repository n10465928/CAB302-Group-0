package vpm.gui_prototype.services;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetService {
    private List<Pet> pets = new ArrayList<>();

    public void addPet(Pet pet) {
        if (pets.size() < 8) { // Only allow up to 8 pets
            pets.add(pet);
        }
    }

    public List<Pet> getAllPets() {
        return pets;
    }
}

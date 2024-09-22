package vpm.gui_prototype.services;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import vpm.gui_prototype.controllers.PetInteractionController;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.util.List;

public class GlobalPetStatService extends ScheduledService<Void> {

    private static GlobalPetStatService instance; // Singleton instance
    private PetManager petManager;
    private int userId;
    private int dirtyCounter; // Counter to track when pet should get dirty
    private PetInteractionController interactionController; // Reference to the UI Controller

    // Private constructor to enforce singleton pattern
    private GlobalPetStatService() {
        petManager = new PetManager(new vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO());
        userId = UserSession.getInstance().getUserId();
        this.setPeriod(Duration.seconds(5)); // Service runs every 5 seconds
        dirtyCounter = 0; // Initialize the dirty counter
    }

    // Get the singleton instance of the service
    public static GlobalPetStatService getInstance() {
        if (instance == null) {
            instance = new GlobalPetStatService();
        }
        return instance;
    }

    // Set the controller instance
    public void setController(PetInteractionController controller) {
        this.interactionController = controller;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                updateAllPetStats();
                return null;
            }
        };
    }

    // Method to update stats for all pets
    private void updateAllPetStats() {
        List<Pet> allUserPets = petManager.getAllUsersPets(userId);

        for (Pet pet : allUserPets) {
            // Decrement happiness and food satisfaction every 5 seconds
            pet.DecreaseHappiness(0.1f); // Decrease happiness
            pet.Feed(-0.1f); // Decrease food satisfaction (Increase hunger)

            // Increment dirty counter and set pet dirty if 30 seconds have passed
            if (dirtyCounter >= 6) { // 6 cycles of 5 seconds = 30 seconds
                if (!pet.GetIsDirty()) {
                    pet.SetIsDirty(true); // Make the pet dirty
                    System.out.println("Updated pet cleanliness: " + pet.GetName() + " | Dirty: " + pet.GetIsDirty());
                }
            }

            // Update the database with new stats
            petManager.updatePet(pet, userId);

            // Log the changes (Optional)
            System.out.println("Updated pet stats: " + pet.GetName() +
                    " | Happiness: " + pet.GetHappiness() +
                    " | Food Satisfaction: " + pet.GetFoodSatisfaction() +
                    " | Dirty: " + pet.GetIsDirty());
        }

        // Increase the dirty counter
        dirtyCounter++;

        // Reset the dirty counter after 30 seconds to allow pets to get dirty again
        if (dirtyCounter >= 6) {
            dirtyCounter = 0;
        }

        // Update the UI via Platform.runLater to ensure it's done on the JavaFX thread
        Platform.runLater(() -> {
            if (interactionController != null) {
                interactionController.refreshUI();
            } else {
                System.err.println("Interaction Controller is not available for UI update.");
            }
        });
    }

    public void startService() {
        if (this.getState() == State.READY || this.getState() == State.SUCCEEDED || this.getState() == State.CANCELLED) {
            this.reset();
        }
        this.start();
    }

    public void stopService() {
        if (this.isRunning()) {
            this.cancel();
        }
    }
}

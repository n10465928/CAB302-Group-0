package vpm.gui_prototype.services;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.util.List;

public class GlobalPetCleanlinessService extends ScheduledService<Void> {

    private static GlobalPetCleanlinessService instance; // Singleton instance
    private PetManager petManager;
    private int userId;

    // Private constructor to enforce singleton pattern
    private GlobalPetCleanlinessService() {
        petManager = new PetManager(new vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO());
        userId = UserSession.getInstance().getUserId();
        this.setPeriod(Duration.seconds(30)); // Service runs every 30 seconds
    }

    // Get the singleton instance of the service
    public static GlobalPetCleanlinessService getInstance() {
        if (instance == null) {
            instance = new GlobalPetCleanlinessService();
        }
        return instance;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                updateAllPetCleanliness();
                return null;
            }
        };
    }

    // Method to update cleanliness state for all pets
    private void updateAllPetCleanliness() {
        List<Pet> allUserPets = petManager.getAllUsersPets(userId);

        for (Pet pet : allUserPets) {
            // Set pet to dirty if not already dirty
            if (!pet.GetIsDirty()) {
                pet.SetIsDirty(true); // Set pet to dirty
                petManager.updatePet(pet, userId); // Update the database
            }

            // Log the changes (Optional)
            System.out.println("Updated pet cleanliness: " + pet.GetName() +
                    " | Dirty: " + pet.GetIsDirty());
        }

        // Ensure that any UI updates are run on the JavaFX application thread
        Platform.runLater(() -> {
            // Notify UI components to refresh if necessary
            // Example: You can implement observer pattern or callback to refresh UI
        });
    }

    // Utility method to start the service with a reset
    public void startService() {
        if (this.getState() != State.READY) {
            this.reset();
        }
        this.start();
    }

    // Utility method to cancel the service
    public void stopService() {
        if (this.isRunning()) {
            this.cancel();
        }
    }
}

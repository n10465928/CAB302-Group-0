package vpm.gui_prototype.services;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton service to manage the background timelines for pet stat decrementing.
 * This service handles the continuous decrementing of pet stats like hunger, happiness,
 * and cleanliness across all views of the application.
 */
public class TimelineService {

    private static TimelineService instance;  // Singleton instance
    private final Map<Pet, PetTimers> petTimersMap = new HashMap<>();  // Map to track timers for each pet
    private PetManager petManager;  // Reference to PetManager for updating pets in the database

    /**
     * Private constructor to enforce singleton pattern.
     */
    private TimelineService() {}

    /**
     * Get the singleton instance of TimelineService.
     *
     * @return the singleton instance.
     */
    public static TimelineService getInstance() {
        if (instance == null) {
            instance = new TimelineService();
        }
        return instance;
    }

    /**
     * Initializes the TimelineService with a reference to the PetManager.
     *
     * @param petManager The PetManager for handling database operations.
     */
    public void initialize(PetManager petManager) {
        this.petManager = petManager;
    }

    /**
     * Starts all the timelines for decrementing stats of the given pet.
     *
     * @param pet The pet to start timers for.
     */
    public void startPetTimers(Pet pet) {
        if (petTimersMap.containsKey(pet)) {
            return;  // Timers already started for this pet
        }
        PetTimers petTimers = new PetTimers(pet);
        petTimers.startAllTimers();
        petTimersMap.put(pet, petTimers);
    }

    /**
     * Stops all the timers for all pets.
     */
    public void stopAllTimers() {
        for (PetTimers timers : petTimersMap.values()) {
            timers.stopAllTimers();
        }
    }

    /**
     * Stops timers for a specific pet.
     *
     * @param pet The pet whose timers to stop.
     */
    public void stopPetTimers(Pet pet) {
        PetTimers timers = petTimersMap.remove(pet);
        if (timers != null) {
            timers.stopAllTimers();
        }
    }

    /**
     * Private class to manage the individual timers for each pet.
     */
    private class PetTimers {
        private final Pet pet;
        private final Timeline happinessTimeline;
        private final Timeline hungerTimeline;

        public PetTimers(Pet pet) {
            this.pet = pet;

            // Create a timeline to decrement happiness
            happinessTimeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHappinessDecrementInterval()), event -> {
                pet.decreaseHappiness(0.1f);
                petManager.updatePet(pet, pet.getUserID());
            }));
            happinessTimeline.setCycleCount(Timeline.INDEFINITE);

            // Create a timeline to decrement hunger
            hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHungerDecrementInterval()), event -> {
                pet.feed(-0.1f);
                petManager.updatePet(pet, pet.getUserID());
            }));
            hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        }

        public void startAllTimers() {
            happinessTimeline.play();
            hungerTimeline.play();
        }

        public void stopAllTimers() {
            happinessTimeline.stop();
            hungerTimeline.stop();
        }
    }
}

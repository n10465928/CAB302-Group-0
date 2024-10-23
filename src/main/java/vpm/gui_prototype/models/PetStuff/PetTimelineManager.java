package vpm.gui_prototype.models.PetStuff;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages timelines for pet happiness and hunger decrements.
 * This singleton class ensures that only one instance handles all pet timelines.
 */
public class PetTimelineManager {

    private static PetTimelineManager instance;  // Singleton instance
    private final Map<Integer, Timeline> happinessTimelines;  // Maps pet IDs to their happiness timelines
    private final Map<Integer, Timeline> hungerTimelines;     // Maps pet IDs to their hunger timelines
    private final PetManager petManager;  // Manages pet data in the database

    /**
     * Private constructor to initialize the PetTimelineManager.
     * Sets up the timelines maps and the PetManager.
     */
    private PetTimelineManager() {
        happinessTimelines = new HashMap<>();
        hungerTimelines = new HashMap<>();
        petManager = new PetManager(new SqlitePetDAO());
    }

    /**
     * Gets the singleton instance of PetTimelineManager.
     * @return The singleton instance.
     */
    public static synchronized PetTimelineManager getInstance() {
        if (instance == null) {
            instance = new PetTimelineManager();
        }
        return instance;
    }

    /**
     * Starts timelines for happiness and hunger decrements for the specified pet.
     * @param pet The pet to manage timelines for.
     */
    public void startTimelines(Pet pet) {
        startHappinessTimeline(pet);
        startHungerTimeline(pet);
    }

    /**
     * Starts the happiness decrement timeline for a specific pet.
     * @param pet The pet whose happiness timeline is to be started.
     */
    private void startHappinessTimeline(Pet pet) {
        stopHappinessTimeline(pet);  // Ensure we don't duplicate timelines
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHappinessDecrementInterval()), event -> {
            pet.decreaseHappiness(0.1f);  // Decrement happiness by 0.1
            petManager.updatePet(pet, pet.getUserID());  // Update pet in the database
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);  // Repeat indefinitely
        timeline.play();  // Start the timeline
        happinessTimelines.put(pet.getPetID(), timeline);  // Store the timeline
    }

    /**
     * Starts the hunger decrement timeline for a specific pet.
     * @param pet The pet whose hunger timeline is to be started.
     */
    private void startHungerTimeline(Pet pet) {
        stopHungerTimeline(pet);  // Ensure we don't duplicate timelines
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHungerDecrementInterval()), event -> {
            pet.feed(-0.1f);  // Decrement hunger by 0.1
            petManager.updatePet(pet, pet.getUserID());  // Update pet in the database
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);  // Repeat indefinitely
        timeline.play();  // Start the timeline
        hungerTimelines.put(pet.getPetID(), timeline);  // Store the timeline
    }

    /**
     * Stops the happiness timeline for a specific pet.
     * @param pet The pet whose happiness timeline is to be stopped.
     */
    public void stopHappinessTimeline(Pet pet) {
        if (happinessTimelines.containsKey(pet.getPetID())) {
            happinessTimelines.get(pet.getPetID()).stop();  // Stop the timeline
        }
    }

    /**
     * Stops the hunger timeline for a specific pet.
     * @param pet The pet whose hunger timeline is to be stopped.
     */
    public void stopHungerTimeline(Pet pet) {
        if (hungerTimelines.containsKey(pet.getPetID())) {
            hungerTimelines.get(pet.getPetID()).stop();  // Stop the timeline
        }
    }

    /**
     * Stops all timelines (happiness and hunger) for a specific pet.
     * @param pet The pet whose timelines are to be stopped.
     */
    public void stopAllTimelines(Pet pet) {
        stopHappinessTimeline(pet);
        stopHungerTimeline(pet);
    }
}

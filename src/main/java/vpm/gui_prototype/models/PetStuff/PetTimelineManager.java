package vpm.gui_prototype.models.PetStuff;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;

import java.util.HashMap;
import java.util.Map;

public class PetTimelineManager {

    private static PetTimelineManager instance;
    private final Map<Integer, Timeline> happinessTimelines;
    private final Map<Integer, Timeline> hungerTimelines;
    private final PetManager petManager;

    private PetTimelineManager() {
        happinessTimelines = new HashMap<>();
        hungerTimelines = new HashMap<>();
        petManager = new PetManager(new SqlitePetDAO());
    }

    public static synchronized PetTimelineManager getInstance() {
        if (instance == null) {
            instance = new PetTimelineManager();
        }
        return instance;
    }

    // Start timelines for happiness and hunger decrements
    public void startTimelines(Pet pet) {
        startHappinessTimeline(pet);
        startHungerTimeline(pet);
    }

    private void startHappinessTimeline(Pet pet) {
        stopHappinessTimeline(pet);  // Ensure we don't duplicate timelines
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHappinessDecrementInterval()), event -> {
            pet.decreaseHappiness(0.1f);  // Decrement happiness
            petManager.updatePet(pet, pet.getUserID());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        happinessTimelines.put(pet.getPetID(), timeline);
    }

    private void startHungerTimeline(Pet pet) {
        stopHungerTimeline(pet);  // Ensure we don't duplicate timelines
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(pet.getHungerDecrementInterval()), event -> {
            pet.feed(-0.1f);  // Decrement hunger
            petManager.updatePet(pet, pet.getUserID());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        hungerTimelines.put(pet.getPetID(), timeline);
    }

    public void stopHappinessTimeline(Pet pet) {
        if (happinessTimelines.containsKey(pet.getPetID())) {
            happinessTimelines.get(pet.getPetID()).stop();
        }
    }

    public void stopHungerTimeline(Pet pet) {
        if (hungerTimelines.containsKey(pet.getPetID())) {
            hungerTimelines.get(pet.getPetID()).stop();
        }
    }

    public void stopAllTimelines(Pet pet) {
        stopHappinessTimeline(pet);
        stopHungerTimeline(pet);
    }
}

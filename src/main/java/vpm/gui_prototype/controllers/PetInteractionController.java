package vpm.gui_prototype.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.IPetDAO;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PetInteractionController {

    private PetManager petManager;
    private IPetDAO petDAO;
    private Timeline happinessTimeline;
    private Timeline hungerTimeline;
    private int userId = UserSession.getInstance().getUserId();

    @FXML
    private Label HappinessField;
    @FXML
    private Label HungerField;
    @FXML
    private Label CleanField;
    @FXML
    private Label picturePetName;
    @FXML
    private ImageView image;
    @FXML
    private Label petName;
    @FXML
    private Label petType;
    @FXML
    private Label petColour;
    @FXML
    private Label petPersonality;
    @FXML
    private Label moodLabel;
    @FXML
    private ProgressBar moodBar;

    private Pet currentPet;

    public void setPet(Pet pet) {
        currentPet = pet;
        updatePetDetails();
        adjustStatsBasedOnTimeDifference();  // Adjust stats based on time since last interaction
        startTimelines();  // Start real-time decrementing
    }

    @FXML
    public void initialize() {
        petManager = new PetManager(new SqlitePetDAO());
        petDAO = new SqlitePetDAO();  // Initialize petDAO
    }

    /**
     * Adjust the pet's stats based on time passed since last interaction.
     */
    private void adjustStatsBasedOnTimeDifference() {
        LocalDateTime lastInteractionTime = petDAO.getLastInteractionTime(currentPet.getPetID());
        LocalDateTime currentTime = LocalDateTime.now();

        if (lastInteractionTime != null) {
            long secondsPassed = ChronoUnit.SECONDS.between(lastInteractionTime, currentTime);

            // Calculate how many decrement intervals have passed
            long happinessIntervalsPassed = secondsPassed / currentPet.getHappinessDecrementInterval();
            long hungerIntervalsPassed = secondsPassed / currentPet.getHungerDecrementInterval();

            // Adjust stats based on intervals passed, rounding to one decimal place
            currentPet.decreaseHappiness(roundToOneDecimal(0.1f * happinessIntervalsPassed));
            currentPet.feed(-roundToOneDecimal(0.1f * hungerIntervalsPassed));

            // Update the database with the adjusted stats
            petManager.updatePet(currentPet, userId);
            refreshUI();
        }

        // Save current time as last interaction time in database
        petDAO.setLastInteractionTime(currentPet.getPetID());
    }

    /**
     * Start timelines to decrement stats in real-time when interacting.
     */
    private void startTimelines() {
        stopTimelines();  // Stop any running timelines first

        happinessTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHappinessDecrementInterval()), event -> decrementHappiness()));
        happinessTimeline.setCycleCount(Timeline.INDEFINITE);
        happinessTimeline.play();

        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHungerDecrementInterval()), event -> decrementHunger()));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        hungerTimeline.play();
    }

    /**
     * Stop both happiness and hunger timelines (e.g., when leaving interaction screen).
     */
    private void stopTimelines() {
        if (happinessTimeline != null) happinessTimeline.stop();
        if (hungerTimeline != null) hungerTimeline.stop();
    }

    private void decrementHappiness() {
        currentPet.decreaseHappiness(0.1f);
        petManager.updatePet(currentPet, userId);  // Update the pet stats in the database
        refreshUI();
    }

    private void decrementHunger() {
        currentPet.feed(-0.1f);
        petManager.updatePet(currentPet, userId);  // Update the pet stats in the database
        refreshUI();
    }

    private void refreshUI() {
        Platform.runLater(this::updatePetDetails);
        updateMood();
    }

    private void updatePetDetails() {
        if (currentPet != null) {
            HappinessField.setText(String.format("%.1f", currentPet.getHappiness()));
            HungerField.setText(String.format("%.1f", currentPet.getFoodSatisfaction()));
            CleanField.setText(currentPet.getIsDirty() ? "Dirty" : "Clean");
            picturePetName.setText(currentPet.getName());
            petName.setText(currentPet.getName());
            petType.setText(currentPet.getType());
            petColour.setText(currentPet.getColour());
            petPersonality.setText(currentPet.getPersonality());
            setImage();
        }
    }

    private void updateMood() {
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;
        if (currentPet.getIsDirty()) moodValue /= 2;
        moodValue /= 10;  // Normalize the mood value to range 0-1

        String moodText = getMoodText(moodValue);
        moodBar.setProgress(moodValue);
        moodLabel.setText(moodText);
    }

    private String getMoodText(double moodValue) {
        if (moodValue <= 0.1) return "I'll Run Away!";
        if (moodValue <= 0.3) return "Very Sad";
        if (moodValue <= 0.5) return "Sad";
        if (moodValue <= 0.7) return "Neutral";
        if (moodValue <= 0.9) return "Happy";
        return "Very Happy";
    }

    private void setImage() {
        String imagePath = getPetImagePath(currentPet.getType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));
            } else {
                image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));
            }
        } else {
            image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));
        }
    }

    private String getPetImagePath(String petType) {
        switch (petType.toLowerCase()) {
            case "cat": return "/assets/cat.png";
            case "dog": return "/assets/dog.png";
            case "fish": return "/assets/fish.png";
            case "bird": return "/assets/bird.png";
            default: return null;
        }
    }

    @FXML
    private void onLeaveInteraction() {
        petDAO.setLastInteractionTime(currentPet.getPetID());  // Save last interaction time
        stopTimelines();  // Stop decrementing
    }

    private void goBackToCollectionView() {
        try {
            onLeaveInteraction();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize();

            Stage stage = (Stage) HappinessField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float roundToOneDecimal(float value) {
        return Math.round(value * 10.0f) / 10.0f;  // Round to one decimal
    }

    @FXML
    private void onInteraction() {
        // Handle interaction when the user leaves or interacts with the pet
        goBackToCollectionView();  // Assuming this navigates back to the collection view
    }

    @FXML
    private void onDelete() {
        if (currentPet != null) {
            petManager.deletePet(currentPet, userId);  // Deletes the pet from the database
            goBackToCollectionView();  // Navigates back to the pet collection screen
        }
    }

    @FXML
    private void onPlay() {
        currentPet.increaseHappiness(1f);  // Increase pet happiness
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    @FXML
    private void onFeed() {
        currentPet.feed(1f);  // Feed the pet (increase food satisfaction)
        petManager.updatePet(currentPet, userId);  // Update the pet's data in the database
        refreshUI();  // Update the UI
    }

    @FXML
    private void onClean() {
        currentPet.clean();  // Clean the pet
        petManager.updatePet(currentPet, userId);  // Update the database with the new status
        refreshUI();  // Update the UI to reflect the change
    }


    @FXML
    private void onEditName() {
        openEditPetFieldView("name");
    }

    @FXML
    private void onEditColour() {
        openEditPetFieldView("colour");
    }

    @FXML
    private void onEditPersonality() {
        openEditPetFieldView("personality");
    }

    private void openEditPetFieldView(String field) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/EditPetFieldView.fxml"));
            Scene scene = new Scene(loader.load());

            EditPetFieldController editPetFieldController = loader.getController();
            editPetFieldController.setPetAndField(currentPet, field);
            editPetFieldController.setOnSaveCallback(this::refreshUI);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Pet " + field.substring(0, 1).toUpperCase() + field.substring(1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
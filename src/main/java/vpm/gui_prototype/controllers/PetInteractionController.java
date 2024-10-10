package vpm.gui_prototype.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.UserStuff.UserSession;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PetInteractionController {

    private PetManager petManager;  // Manager for pet-related database operations
    private IUserDAO userDAO;  // DAO for user-related database operations
    private Timeline happinessTimeline;  // Timeline for decrementing happiness
    private Timeline hungerTimeline;  // Timeline for decrementing hunger
    int userId = UserSession.getInstance().getUserId();  // Retrieve userId from the session


    @FXML
    private Label HappinessField;
    @FXML
    private Label HungerField;
    @FXML
    private Label CleanField;
    @FXML
    private Label picturePetName;
    @FXML
    private Label picturePetType;
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

    private Pet currentPet;  // Holds the current pet object being interacted with

    public void setPet(Pet pet) {
        currentPet = pet;
        updatePetDetails();
        startTimelines();  // Start the decrement logic for happiness and hunger
    }

    @FXML
    public void initialize() {
        petManager = new PetManager(new SqlitePetDAO());
        userDAO = new SqliteUserDAO();

        LocalDateTime lastInteractionTime = userDAO.getLastInteractionTime(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        if (lastInteractionTime != null) {
            long secondsPassed = ChronoUnit.SECONDS.between(lastInteractionTime, currentTime);
            long happinessIntervalsPassed = secondsPassed / currentPet.getHappinessDecrementInterval();
            long hungerIntervalsPassed = secondsPassed / currentPet.getHungerDecrementInterval();

            adjustPetStatsBasedOnTime(happinessIntervalsPassed, hungerIntervalsPassed);
        }

        // Update the last interaction time to the current time in the database
        userDAO.setLastInteractionTime(userId, currentTime);
    }

    private void adjustPetStatsBasedOnTime(long happinessIntervalsPassed, long hungerIntervalsPassed) {
        if (currentPet != null) {
            currentPet.DecreaseHappiness(0.1f * happinessIntervalsPassed);
            currentPet.Feed(-0.1f * hungerIntervalsPassed);
            petManager.updatePet(currentPet, userId);  // Update the database with the adjusted stats
            refreshUI();
        }
    }

    // Start separate Timelines for happiness and hunger based on their individual intervals
    private void startTimelines() {
        if (happinessTimeline != null) {
            happinessTimeline.stop();
        }
        if (hungerTimeline != null) {
            hungerTimeline.stop();
        }

        // Happiness timeline (based on pet-specific decrement interval)
        happinessTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHappinessDecrementInterval()), event -> {
            decrementHappiness();
        }));
        happinessTimeline.setCycleCount(Timeline.INDEFINITE);
        happinessTimeline.play();

        // Hunger timeline (based on pet-specific decrement interval)
        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHungerDecrementInterval()), event -> {
            decrementHunger();
        }));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        hungerTimeline.play();
    }

    // Decrement the pet's happiness based on the pet's custom happiness interval
    private void decrementHappiness() {
        if (currentPet != null) {
            currentPet.DecreaseHappiness(0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
    }

    // Decrement the pet's hunger based on the pet's custom hunger interval
    private void decrementHunger() {
        if (currentPet != null) {
            currentPet.Feed(-0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
    }

    private void updatePetDetails() {
        if (currentPet != null) {
            HappinessField.setText(String.format("%.1f", currentPet.GetHappiness()));
            HungerField.setText(String.format("%.1f", currentPet.GetFoodSatisfaction()));
            CleanField.setText(currentPet.GetIsDirty() ? "Dirty" : "Clean");
            picturePetName.setText(currentPet.GetName());
            picturePetType.setText(currentPet.GetType());
            petName.setText(currentPet.GetName());
            petType.setText(currentPet.GetType());
            petColour.setText(currentPet.GetColour());
            petPersonality.setText(currentPet.GetPersonality());
            setImage();
        }
    }

    // Refresh UI manually to reflect any updates
    public void refreshUI() {
        Platform.runLater(this::updatePetDetails);  // Ensure UI updates happen on the JavaFX Application Thread
    }

    // Set the pet's image based on its type
    private void setImage() {
        String imagePath = getPetImagePath(currentPet.GetType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));
            } else {
                image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));  // Default image
            }
        } else {
            image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));  // Default image
        }
    }

    // Return the correct image path based on the pet's type
    private String getPetImagePath(String petType) {
        switch (petType.toLowerCase()) {
            case "cat":
                return "/assets/cat.png";
            case "dog":
                return "/assets/dog.png";
            case "fish":
                return "/assets/fish.png";
            case "bird":
                return "/assets/bird.png";
            default:
                return null;
        }
    }

    // Action handler for when the user plays with the pet
    @FXML
    private void onPlay() {
        currentPet.IncreaseHappiness(1f);  // Increase pet happiness
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    // Action handler for when the user feeds the pet
    @FXML
    private void onFeed() {
        currentPet.Feed(1f);  // Feed the pet (increase food satisfaction)
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    // Action handler for when the user cleans the pet
    @FXML
    private void onClean() {
        currentPet.Clean();  // Clean the pet (set to clean)
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    // Navigate back to the pet collection view
    @FXML
    private void onInteraction() {
        goBackToCollectionView();
    }

    // Action handler for deleting the current pet
    @FXML
    private void onDelete() {
        if (currentPet != null) {
            petManager.deletePet(currentPet, userId);
            goBackToCollectionView();
        }
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

    // Method to navigate back to the pet collection view
    private void goBackToCollectionView() {
        try {
            userDAO.setLastInteractionTime(userId, LocalDateTime.now());
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
}

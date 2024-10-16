package vpm.gui_prototype.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import java.util.function.Consumer;

/**
 * Controller for pet interaction, handling the pet's state,
 * UI updates, and user interactions such as playing, feeding, and cleaning the pet.
 */
public class PetInteractionController {

    private PetManager petManager;  // Manager for pet-related database operations
    private IUserDAO userDAO;  // DAO for user-related database operations
    private Timeline happinessTimeline;  // Timeline for decrementing happiness
    private Timeline hungerTimeline;  // Timeline for decrementing hunger
    private int userId = UserSession.getInstance().getUserId();  // Retrieve userId from the session

    // UI Elements
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
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    private Pet currentPet;  // Holds the current pet object being interacted with

    /**
     * Sets the current pet and initializes the UI with its details.
     *
     * @param pet The pet to be interacted with.
     */
    public void setPet(Pet pet) {
        currentPet = pet;
        updatePetDetails();
        updateMood();
        startTimelines();  // Start the decrement logic for happiness and hunger
    }

    /**
     * Initializes the controller and updates the pet stats based on the last interaction time.
     */
    @FXML
    public void initialize() {
        petManager = new PetManager(new SqlitePetDAO());
        userDAO = new SqliteUserDAO();

        // Get the last interaction time and calculate time since then
        LocalDateTime lastInteractionTime = userDAO.getLastInteractionTime(userId);
        LocalDateTime currentTime = LocalDateTime.now();

        if (lastInteractionTime != null) {
            long secondsPassed = ChronoUnit.SECONDS.between(lastInteractionTime, currentTime);
            long happinessIntervalsPassed = secondsPassed / currentPet.getHappinessDecrementInterval();
            long hungerIntervalsPassed = secondsPassed / currentPet.getHungerDecrementInterval();

            // Adjust pet stats based on the time passed
            adjustPetStatsBasedOnTime(happinessIntervalsPassed, hungerIntervalsPassed);
        }

        // Update the last interaction time to the current time in the database
        userDAO.setLastInteractionTime(userId, currentTime);
    }

    /**
     * Adjusts the pet's happiness and hunger based on the time intervals passed since the last interaction.
     *
     * @param happinessIntervalsPassed The number of happiness intervals that have passed.
     * @param hungerIntervalsPassed The number of hunger intervals that have passed.
     */
    private void adjustPetStatsBasedOnTime(long happinessIntervalsPassed, long hungerIntervalsPassed) {
        if (currentPet != null) {
            currentPet.decreaseHappiness(0.1f * happinessIntervalsPassed);
            currentPet.feed(-0.1f * hungerIntervalsPassed);
            petManager.updatePet(currentPet, userId);  // Update the database with the adjusted stats
            refreshUI();  // Refresh the UI to reflect changes
        }
    }

    /**
     * Starts separate Timelines for decrementing happiness and hunger based on their specific intervals.
     */
    private void startTimelines() {
        // Stop any existing timelines to prevent overlap
        if (happinessTimeline != null) {
            happinessTimeline.stop();
        }
        if (hungerTimeline != null) {
            hungerTimeline.stop();
        }

        // Create and start the happiness timeline
        happinessTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHappinessDecrementInterval()), event -> {
            decrementHappiness();
        }));
        happinessTimeline.setCycleCount(Timeline.INDEFINITE);
        happinessTimeline.play();

        // Create and start the hunger timeline
        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHungerDecrementInterval()), event -> {
            decrementHunger();
        }));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        hungerTimeline.play();
    }

    /**
     * Decrements the pet's happiness and updates the UI and database accordingly.
     */
    private void decrementHappiness() {
        if (currentPet != null) {
            currentPet.decreaseHappiness(0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
    }

    /**
     * Decrements the pet's hunger and updates the UI and database accordingly.
     */
    private void decrementHunger() {
        if (currentPet != null) {
            currentPet.feed(-0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
    }

    /**
     * Updates the UI with the current pet's details.
     */
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
            setImage();  // Set the pet image
        }
    }

    /**
     * Updates the mood label and progress bar based on the pet's happiness and hunger.
     */
    private void updateMood() {
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;

        if (currentPet.getIsDirty()) {
            moodValue /= 2;  // Mood decreases if the pet is dirty
        }
        moodValue /= 10;  // Scale mood value to fit progress bar

        // Determine mood text based on mood value
        String moodText;
        if (moodValue >= 0 && moodValue <= 0.1) {
            moodText = "I'll Run Away!";
        } else if (moodValue > 0.1 && moodValue <= 0.3) {
            moodText = "Very Sad";
        } else if (moodValue > 0.3 && moodValue <= 0.5) {
            moodText = "Sad";
        } else if (moodValue > 0.5 && moodValue <= 0.7) {
            moodText = "Neutral";
        } else if (moodValue > 0.7 && moodValue <= 0.9) {
            moodText = "Happy";
        } else if (moodValue > 0.9 && moodValue <= 1.0) {
            moodText = "Very Happy";
        } else {
            moodText = "Unknown";  // Handle unexpected values
        }

        moodBar.setProgress(moodValue); // Ensure mood value is between 0.0 and 1.0
        moodLabel.setText(moodText);
    }

    /**
     * Refreshes the UI to reflect any updates to the pet's status.
     */
    public void refreshUI() {
        Platform.runLater(this::updatePetDetails);  // Ensure UI updates occur on the JavaFX Application Thread
        updateMood();
    }

    /**
     * Sets the pet's image based on its type.
     */
    private void setImage() {
        String imagePath = getPetImagePath(currentPet.getType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));
            } else {
                image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));  // Default image if not found
            }
        } else {
            image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));  // Default image if type not recognized
        }
    }

    /**
     * Returns the correct image path based on the pet's type.
     *
     * @param petType The type of the pet (e.g., cat, dog).
     * @return The image path corresponding to the pet type.
     */
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
                return null;  // Return null if the type is unrecognized
        }
    }

    /**
     * Action handler for when the user plays with the pet.
     */
    @FXML
    private void onPlay() {
        currentPet.increaseHappiness(1f);  // Increase pet happiness
        petManager.updatePet(currentPet, userId);  // Update the database
        refreshUI();  // Refresh the UI
    }

    /**
     * Action handler for when the user feeds the pet.
     */
    @FXML
    private void onFeed() {
        currentPet.feed(1f);  // Increase food satisfaction
        petManager.updatePet(currentPet, userId);  // Update the database
        refreshUI();  // Refresh the UI
    }

    /**
     * Action handler for when the user cleans the pet.
     */
    @FXML
    private void onClean() {
        currentPet.clean();  // Clean the pet
        petManager.updatePet(currentPet, userId);  // Update the database
        refreshUI();  // Refresh the UI
    }

    /**
     * Navigate back to the pet collection view.
     */
    @FXML
    private void onBack() {
        goBackToCollectionView();
    }

    /**
     * Action handler for deleting the current pet.
     */
    @FXML
    private void onDelete() throws IOException {
        navigateToDeleteConfirmationView(currentPet, this::onDeleteConfirmed);
    }

    /**
     * Callback for when the delete confirmation dialog is closed.
     *
     * @param confirmed Indicates whether the deletion was confirmed.
     */
    private void onDeleteConfirmed(boolean confirmed) {
        if (confirmed) {
            petManager.deletePet(currentPet, userId);  // Delete the pet if confirmed
            refreshUI();  // Refresh the UI
            goBackToCollectionView();  // Navigate back to the collection view
        }
    }

    /**
     * Opens the edit view for the pet's name.
     */
    @FXML
    private void onEditName() {
        openEditPetFieldView("name");
    }

    /**
     * Opens the edit view for the pet's colour.
     */
    @FXML
    private void onEditColour() {
        openEditPetFieldView("colour");
    }

    /**
     * Opens the edit view for the pet's personality.
     */
    @FXML
    private void onEditPersonality() {
        openEditPetFieldView("personality");
    }

    /**
     * Opens the edit view for a specific pet field.
     *
     * @param field The field to edit (name, colour, or personality).
     */
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

    /**
     * Navigates to the delete confirmation view.
     *
     * @param pet The pet to confirm deletion.
     * @param callback The callback to execute after confirmation.
     * @throws IOException If the FXML file cannot be loaded.
     */
    private void navigateToDeleteConfirmationView(Pet pet, Consumer<Boolean> callback) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/DeleteConfirmationView.fxml"));
        Parent root = loader.load();

        DeleteConfirmationController deleteConfirmationController = loader.getController();
        deleteConfirmationController.setPet(pet);  // Set the pet for deletion confirmation
        deleteConfirmationController.setOnConfirmationCallback(callback); // Set the callback

        // Set up the stage for the confirmation dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Confirm Delete");
        stage.show();
    }

    /**
     * Navigates back to the pet collection view.
     */
    private void goBackToCollectionView() {
        try {
            userDAO.setLastInteractionTime(userId, LocalDateTime.now());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize();  // Initialize the collection view

            Stage stage = (Stage) HappinessField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

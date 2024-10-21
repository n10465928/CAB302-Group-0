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
import vpm.gui_prototype.models.DatabaseStuff.PetData.IPetDAO;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.FoodStuff.*;
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
 * Controller for pet interaction, managing the pet's state,
 * UI updates, and user interactions like playing, feeding, and cleaning the pet.
 */
public class PetInteractionController {

    private PetManager petManager;  // Manager for pet-related database operations
    private IPetDAO petDAO;
    //private IUserDAO userDAO;  // DAO for user-related database operations
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
        currentPet = pet;  // Store the current pet
        updatePetDetails();  // Update the UI with pet details
        adjustStatsBasedOnTimeDifference();  // Adjust stats based on time since last interaction
        startTimelines();  // Start the decrement logic for happiness and hunger
    }

    /**
     * Initializes the controller and updates the pet stats based on the last interaction time.
     */
    @FXML
    public void initialize() {
        petManager = new PetManager(new SqlitePetDAO());
        petDAO = new SqlitePetDAO();  // Initialize petDAO
        //userDAO = new SqliteUserDAO();
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
        // Update the pet stats in the database
        petManager.updatePet(currentPet, userId);
        refreshUI();  // Refresh the UI to show the new values
    }

    private void decrementHunger() {
        currentPet.feed(-0.1f);
        // Update the pet stats in the database
        petManager.updatePet(currentPet, userId);
        refreshUI();  // Refresh the UI to show the new values
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
            setImage();
        }
    }

    /**
     * Updates the mood label and progress bar based on the pet's happiness and hunger.
     */
    private void updateMood() {
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;
        if (currentPet.getIsDirty()) moodValue /= 2;
        moodValue /= 10;  // Normalize the mood value to range 0-1

        String moodText = getMoodText(moodValue);
        moodBar.setProgress(moodValue);
        moodLabel.setText(moodText);
    }

    /**
     * Determines the mood text based on the mood value.
     *
     * @param moodValue The calculated mood value.
     * @return A string representing the mood.
     */
    private String getMoodText(double moodValue) {
        if (moodValue <= 0.1) return "I'll Run Away!";
        if (moodValue <= 0.3) return "Very Sad";
        if (moodValue <= 0.5) return "Sad";
        if (moodValue <= 0.7) return "Neutral";
        if (moodValue <= 0.9) return "Happy";
        return "Very Happy";
    }

    /**
     * Refreshes the UI to reflect any updates to the pet's status.
     */
    private void refreshUI() {
        Platform.runLater(this::updatePetDetails);
        updateMood();
    }

    /**
     * Sets the pet's image based on its type.
     */
    private void setImage() {
        String imagePath = getPetImagePath(currentPet.getType());  // Get the image path based on pet type
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));  // Set the pet image if found
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

    @FXML
    private void onLeaveInteraction() {
        petDAO.setLastInteractionTime(currentPet.getPetID());  // Save last interaction time
        stopTimelines();  // Stop decrementing
    }

    // Action handlers for playing with the pet
    @FXML
    private void onPlay1() {
        onPlay("Dog");
    }
    @FXML
    private void onPlay2() {
        onPlay("Cat");
    }
    @FXML
    private void onPlay3() {
        onPlay("Bird");
    }
    @FXML
    private void onPlay4() {
        onPlay("Fish");
    }

    /**
     * Action handler for when the user plays with the pet.
     *
     * @param type The type of pet being played with.
     */
    private void onPlay(String type) {
        currentPet.playWtihPet(type, 1f);  // Increase pet happiness
        petManager.updatePet(currentPet, userId);  // Update the database with the new stats
        refreshUI();  // Refresh the UI
    }

    // Action handlers for feeding the pet
    @FXML
    private void onFeed1() {
        Food food = new Bone(1.0f, 3.0f);
        onFeed(food);
    }
    @FXML
    private void onFeed2() {
        Food food = new Milk(2.0f, 3.0f);
        onFeed(food);
    }
    @FXML
    private void onFeed3() {
        Food food = new Seed(1.0f, 1.0f);
        onFeed(food);
    }
    @FXML
    private void onFeed4() {
        Food food = new Pallets(2.0f, 2.0f);
        onFeed(food);
    }

    /**
     * Action handler for when the user feeds the pet.
     *
     * @param food The food item to be fed to the pet.
     */
    private void onFeed(Food food) {
        currentPet.feed(food);  // Feed the pet (increase food satisfaction)
        petManager.updatePet(currentPet, userId);  // Update the database with the new stats
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
        goBackToCollectionView();  // Navigate back to collection view
    }

    /**
     * Action handler for deleting the current pet.
     */
    @FXML
    private void onDelete() throws IOException {
        navigateToDeleteConfirmationView(currentPet, this::onDeleteConfirmed);  // Open delete confirmation view
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
        openEditPetFieldView("name");  // Open edit view for name
    }

    /**
     * Opens the edit view for the pet's colour.
     */
    @FXML
    private void onEditColour() {
        openEditPetFieldView("colour");  // Open edit view for colour
    }

    /**
     * Opens the edit view for the pet's personality.
     */
    @FXML
    private void onEditPersonality() {
        openEditPetFieldView("personality");  // Open edit view for personality
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
            editPetFieldController.setPetAndField(currentPet, field);  // Set the pet and field to edit
            editPetFieldController.setOnSaveCallback(this::refreshUI);  // Set the callback for save action

            // Set up the stage for the edit view
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Pet " + field.substring(0, 1).toUpperCase() + field.substring(1));  // Title formatting
            stage.show();  // Show the edit view
        } catch (IOException e) {
            e.printStackTrace();  // Handle any loading errors
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
        deleteConfirmationController.setOnConfirmationCallback(callback); // Set the callback for confirmation

        // Set up the stage for the confirmation dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Confirm Delete");  // Title for confirmation dialog
        stage.show();  // Show the confirmation dialog
    }

    /**
     * Navigates back to the pet collection view.
     */
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


}

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
import vpm.gui_prototype.services.SoundManager; // Import SoundManager based on its package

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
    private IPetDAO petDAO;  // Data access object for pet data
    private Timeline happinessTimeline;  // Timeline for decrementing happiness
    private Timeline hungerTimeline;  // Timeline for decrementing hunger
    private int userId = UserSession.getInstance().getUserId();  // Retrieve userId from the session

    // UI Elements
    @FXML
    private Label HappinessField;  // Display for happiness level
    @FXML
    private Label HungerField;  // Display for hunger level
    @FXML
    private Label CleanField;  // Display for cleanliness status
    @FXML
    private Label picturePetName;  // Label for pet's name in picture
    @FXML
    private ImageView image;  // ImageView for pet image
    @FXML
    private Label petName;  // Display for pet's name
    @FXML
    private Label petType;  // Display for pet's type
    @FXML
    private Label petColour;  // Display for pet's colour
    @FXML
    private Label petPersonality;  // Display for pet's personality
    @FXML
    private Label moodLabel;  // Label for displaying mood
    @FXML
    private ProgressBar moodBar;  // Progress bar for mood indication
    @FXML
    private Button deleteButton;  // Button to delete the pet
    @FXML
    private Button backButton;  // Button to go back to the previous view

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
        petManager = new PetManager(new SqlitePetDAO());  // Initialize PetManager with SQL DAO
        petDAO = new SqlitePetDAO();  // Initialize petDAO
    }

    /**
     * Adjusts the pet's stats based on time passed since the last interaction.
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
            refreshUI();  // Refresh the UI to show the updated stats
        }

        // Save current time as last interaction time in the database
        petDAO.setLastInteractionTime(currentPet.getPetID());
    }

    /**
     * Starts timelines to decrement stats in real-time when interacting.
     */
    private void startTimelines() {
        stopTimelines();  // Stop any running timelines first

        // Timeline to decrement happiness
        happinessTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHappinessDecrementInterval()), event -> decrementHappiness()));
        happinessTimeline.setCycleCount(Timeline.INDEFINITE);
        happinessTimeline.play();

        // Timeline to decrement hunger
        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHungerDecrementInterval()), event -> decrementHunger()));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        hungerTimeline.play();
    }

    /**
     * Stops both happiness and hunger timelines (e.g., when leaving interaction screen).
     */
    private void stopTimelines() {
        if (happinessTimeline != null) happinessTimeline.stop();  // Stop happiness timeline if running
        if (hungerTimeline != null) hungerTimeline.stop();  // Stop hunger timeline if running
    }

    /**
     * Decrements the pet's happiness and updates the database.
     */
    private void decrementHappiness() {
        currentPet.decreaseHappiness(0.1f);  // Decrease happiness
        petManager.updatePet(currentPet, userId);  // Update the pet stats in the database
        refreshUI();  // Refresh the UI to show the new values
    }

    /**
     * Decrements the pet's hunger and updates the database.
     */
    private void decrementHunger() {
        currentPet.feed(-0.1f);  // Decrease food satisfaction
        petManager.updatePet(currentPet, userId);  // Update the pet stats in the database
        refreshUI();  // Refresh the UI to show the new values
    }

    /**
     * Updates the UI with the current pet's details.
     */
    private void updatePetDetails() {
        if (currentPet != null) {
            // Update the display fields with current pet stats
            HappinessField.setText(String.format("%.1f", currentPet.getHappiness()));
            HungerField.setText(String.format("%.1f", currentPet.getFoodSatisfaction()));
            CleanField.setText(currentPet.getIsDirty() ? "Dirty" : "Clean");
            picturePetName.setText(currentPet.getName());
            petName.setText(currentPet.getName());
            petType.setText(currentPet.getType());
            petColour.setText(currentPet.getColour());
            petPersonality.setText(currentPet.getPersonality());
            setImage();  // Set the pet image based on type and color
        }
    }

    /**
     * Updates the mood label and progress bar based on the pet's happiness and hunger.
     */
    private void updateMood() {
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;  // Calculate mood value
        if (currentPet.getIsDirty()) moodValue /= 2;  // Reduce mood value if pet is dirty
        moodValue /= 10;  // Normalize the mood value to range 0-1

        String moodText = getMoodText(moodValue);  // Get mood text based on value
        moodBar.setProgress(moodValue);  // Update mood progress bar
        moodLabel.setText(moodText);  // Update mood label
    }

    /**
     * Determines the mood text based on the mood value.
     *
     * @param moodValue The calculated mood value.
     * @return A string representing the mood.
     */
    private String getMoodText(double moodValue) {
        // Return mood description based on mood value
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
        Platform.runLater(this::updatePetDetails);  // Ensure UI updates on the JavaFX Application Thread
        updateMood();  // Update the mood display
    }

    /**
     * Sets the pet's image based on its type and color.
     */
    private void setImage() {
        String imagePath = getPetImagePath(currentPet.getType(), currentPet.getColour());  // Get the image path based on pet type and color
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);  // Load the image
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
     * Returns the correct image path based on the pet's type and color.
     *
     * @param petType The type of the pet (e.g., cat, dog).
     * @param petColour The color of the pet (e.g., brown, red).
     * @return The image path corresponding to the pet type and color.
     */
    private String getPetImagePath(String petType, String petColour) {
        return String.format("/assets/%s_%s.png", petType.toLowerCase(), petColour.toLowerCase());  // Format the image path
    }

    /**
     * Handles actions when leaving the interaction.
     */
    @FXML
    private void onLeaveInteraction() {
        petDAO.setLastInteractionTime(currentPet.getPetID());  // Save last interaction time
        stopTimelines();  // Stop decrementing
    }

    // Action handlers for playing with the pet
    @FXML
    private void onPlay1() { onPlay("Dog"); }
    @FXML
    private void onPlay2() { onPlay("Cat"); }
    @FXML
    private void onPlay3() { onPlay("Bird"); }
    @FXML
    private void onPlay4() { onPlay("Fish"); }

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
        Food food = new Bone(1.0f, 3.0f);  // Create a Bone food item
        onFeed(food);  // Feed the pet
    }
    @FXML
    private void onFeed2() {
        Food food = new Milk(2.0f, 3.0f);  // Create a Milk food item
        onFeed(food);  // Feed the pet
    }
    @FXML
    private void onFeed3() {
        Food food = new Seed(1.0f, 1.0f);  // Create a Seed food item
        onFeed(food);  // Feed the pet
    }
    @FXML
    private void onFeed4() {
        Food food = new Pellets(2.0f, 2.0f);  // Create a Pallets food item
        onFeed(food);  // Feed the pet
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
        SoundManager.playSound(currentPet.getType(), "leave");  // Play leaving sound
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
            onLeaveInteraction();  // Save last interaction
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize();  // Initialize collection view

            Stage stage = (Stage) HappinessField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Collection");  // Set title for collection view
        } catch (IOException e) {
            e.printStackTrace();  // Handle loading errors
        }
    }

    /**
     * Rounds a float value to one decimal place.
     *
     * @param value The value to round.
     * @return The rounded value.
     */
    private float roundToOneDecimal(float value) {
        return Math.round(value * 10.0f) / 10.0f;  // Round to one decimal
    }
}

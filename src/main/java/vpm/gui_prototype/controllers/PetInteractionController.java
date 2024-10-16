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
import vpm.gui_prototype.models.FoodStuff.Food;
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
        currentPet = pet;  // Store the current pet
        updatePetDetails();  // Update the UI with pet details
        updateMood();  // Update the mood display
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
            // Decrease happiness and hunger based on time passed
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
            decrementHappiness();  // Decrement happiness at specified intervals
        }));
        happinessTimeline.setCycleCount(Timeline.INDEFINITE);
        happinessTimeline.play();  // Start the timeline

        // Create and start the hunger timeline
        hungerTimeline = new Timeline(new KeyFrame(Duration.seconds(currentPet.getHungerDecrementInterval()), event -> {
            decrementHunger();  // Decrement hunger at specified intervals
        }));
        hungerTimeline.setCycleCount(Timeline.INDEFINITE);
        hungerTimeline.play();  // Start the timeline
    }

    /**
     * Decrements the pet's happiness and updates the UI and database accordingly.
     */
    private void decrementHappiness() {
        if (currentPet != null) {
            currentPet.decreaseHappiness(0.1f);  // Decrease happiness
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();  // Refresh the UI
        }
    }

    /**
     * Decrements the pet's hunger and updates the UI and database accordingly.
     */
    private void decrementHunger() {
        if (currentPet != null) {
            currentPet.feed(-0.1f);  // Decrease hunger
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();  // Refresh the UI
        }
    }

    /**
     * Updates the UI with the current pet's details.
     */
    private void updatePetDetails() {
        if (currentPet != null) {
            // Set UI elements to reflect pet's current stats
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
        // Calculate the mood value based on happiness and food satisfaction
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;

        // Mood decreases if the pet is dirty
        if (currentPet.getIsDirty()) {
            moodValue /= 2;
        }
        moodValue /= 10;  // Scale mood value to fit progress bar

        // Determine mood text based on mood value
        String moodText = determineMoodText(moodValue);

        // Update UI elements with calculated mood
        moodBar.setProgress(moodValue); // Ensure mood value is between 0.0 and 1.0
        moodLabel.setText(moodText);
    }

    /**
     * Determines the mood text based on the mood value.
     *
     * @param moodValue The calculated mood value.
     * @return A string representing the mood.
     */
    private String determineMoodText(double moodValue) {
        if (moodValue >= 0 && moodValue <= 0.1) {
            return "I'll Run Away!";
        } else if (moodValue > 0.1 && moodValue <= 0.3) {
            return "Very Sad";
        } else if (moodValue > 0.3 && moodValue <= 0.5) {
            return "Sad";
        } else if (moodValue > 0.5 && moodValue <= 0.7) {
            return "Neutral";
        } else if (moodValue > 0.7 && moodValue <= 0.9) {
            return "Happy";
        } else if (moodValue > 0.9 && moodValue <= 1.0) {
            return "Very Happy";
        } else {
            return "Unknown";  // Handle unexpected values
        }
    }

    /**
     * Refreshes the UI to reflect any updates to the pet's status.
     */
    public void refreshUI() {
        // Ensure UI updates occur on the JavaFX Application Thread
        Platform.runLater(this::updatePetDetails);
        updateMood();  // Update mood display
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
        Food food = new Food(1.0f, "Dog");
        onFeed(food);
    }
    @FXML
    private void onFeed2() {
        Food food = new Food(1.0f, "Cat");
        onFeed(food);
    }
    @FXML
    private void onFeed3() {
        Food food = new Food(1.0f, "Bird");
        onFeed(food);
    }
    @FXML
    private void onFeed4() {
        Food food = new Food(1.0f, "Fish");
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
            userDAO.setLastInteractionTime(userId, LocalDateTime.now());  // Update last interaction time
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize();  // Initialize the collection view

            Stage stage = (Stage) HappinessField.getScene().getWindow();  // Get current stage
            stage.setScene(scene);  // Set the new scene
            stage.setTitle("Pet Collection");  // Title for the collection view
        } catch (IOException e) {
            e.printStackTrace();  // Handle any loading errors
        }
    }
}

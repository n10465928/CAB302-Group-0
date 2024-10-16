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

    public void setPet(Pet pet) {
        currentPet = pet;
        updatePetDetails();
        updateMood();
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
            currentPet.decreaseHappiness(0.1f * happinessIntervalsPassed);
            currentPet.feed(-0.1f * hungerIntervalsPassed);
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
            currentPet.decreaseHappiness(0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
    }

    // Decrement the pet's hunger based on the pet's custom hunger interval
    private void decrementHunger() {
        if (currentPet != null) {
            currentPet.feed(-0.1f);
            petManager.updatePet(currentPet, userId);  // Update the database with the new stats
            refreshUI();
        }
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

    private void updateMood( ) {
        double moodValue = currentPet.getHappiness() * 0.4 + currentPet.getFoodSatisfaction() * 0.6;

        if(currentPet.getIsDirty()){
            moodValue /=2;
        }
        moodValue /=10;
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
            moodText = "Unknown";  // For unexpected values
        }
        moodBar.setProgress(moodValue); // mood value should be between 0.0 and 1.0
        moodLabel.setText(moodText);
    }

    // Refresh UI manually to reflect any updates
    public void refreshUI() {
        Platform.runLater(this::updatePetDetails);  // Ensure UI updates happen on the JavaFX Application Thread
        updateMood();
    }

    // Set the pet's image based on its type
    private void setImage() {
        String imagePath = getPetImagePath(currentPet.getType());
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
        currentPet.increaseHappiness(1f);  // Increase pet happiness
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }
    //onfeed builder for each button
    @FXML
    private void onFeed1(){
        Food food = new Food(1.0f, "Dog");
        onFeed(food);
    }
    @FXML
    private void onFeed2(){
        Food food = new Food(1.0f, "Cat");
        onFeed(food);
    }
    @FXML
    private void onFeed3(){
        Food food = new Food(1.0f, "Bird");
        onFeed(food);
    }
    @FXML
    private void onFeed4(){
        Food food = new Food(1.0f, "Fish");
        onFeed(food);
    }
    // Action handler for when the user feeds the pet
    private void onFeed(Food food) {
        currentPet.feed(food);  // Feed the pet (increase food satisfaction)
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    // Action handler for when the user cleans the pet
    @FXML
    private void onClean() {
        currentPet.clean();  // Clean the pet (set to clean)
        petManager.updatePet(currentPet, userId);
        refreshUI();
    }

    // Navigate back to the pet collection view
    @FXML
    private void onBack() {
        goBackToCollectionView();
    }

    // Action handler for deleting the current pet
    @FXML
    private void onDelete() throws IOException {
        navigateToDeleteConformationView(currentPet, this::onDeleteConfirmed);
    }

    private void onDeleteConfirmed(boolean confirmed) {
        if (confirmed) {
            // Perform the deletion logic here if confirmed
            petManager.deletePet(currentPet, userId);  // Assuming you have a method to delete the pet
            refreshUI();
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

    private void navigateToDeleteConformationView(Pet pet, Consumer<Boolean> callback) throws IOException {
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
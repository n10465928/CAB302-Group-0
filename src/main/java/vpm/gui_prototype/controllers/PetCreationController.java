package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.*;
import vpm.gui_prototype.models.UserStuff.UserSession;
import vpm.gui_prototype.services.SoundManager; // Import SoundManager based on its package

import java.io.IOException;

/**
 * Controller for the PetCreationView.
 * Handles the creation of new pet entries by managing user inputs and interactions.
 */
public class PetCreationController {

    private PetManager petManager; // Manager for pet-related database operations
    private int userId = UserSession.getInstance().getUserId(); // Current user ID

    @FXML
    private TextField petNameField; // Text field for entering pet name

    @FXML
    private ComboBox<String> petTypeComboBox; // ComboBox for selecting pet type

    @FXML
    private TextField petAgeField; // Text field for entering pet age

    @FXML
    private Label messageLabel; // Label for showing error messages

    /**
     * Constructor for PetCreationController. Initializes the PetManager.
     */
    public PetCreationController() {
        petManager = new PetManager(new SqlitePetDAO()); // Initialize PetManager with SQLite DAO
    }

    /**
     * Initializes the pet type ComboBox with available pet types.
     */
    @FXML
    public void initialize() {
        petTypeComboBox.getItems().addAll("Dog", "Cat", "Bird", "Fish"); // Add pet types to ComboBox
    }

    /**
     * Evaluates if the pet's name meets specified conditions:
     * - Length between 2 and 12 characters
     * - No special characters allowed
     *
     * @param petName the pet name input string
     * @return a validation message indicating success or failure
     */
    public String evaluatePetName(String petName) {
        if (petName.length() < 2 || petName.length() > 12) {
            return "Your pet's name must be from 2 to 12 characters long"; // Length error
        }
        for (int i = 0; i < petName.length(); i++) {
            char c = petName.charAt(i);
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ') {
                return "Your pet's name must not contain special characters"; // Special character error
            }
        }
        return "Good"; // Validation successful
    }

    /**
     * Handles the pet creation process when the "Create Pet" button is pressed.
     * Validates inputs and adds the new pet to the database.
     */
    @FXML
    private void onCreatePet() {
        String petName = petNameField.getText();
        String petType = petTypeComboBox.getValue();
        String petAgeText = petAgeField.getText();

        if (petName == null || petName.isEmpty() || petType == null || petType.isEmpty() || petAgeText == null || petAgeText.isEmpty()) {
            showErrorMessage("All fields must be filled out.");
            return;
        }

        int petAge;
        try {
            petAge = Integer.parseInt(petAgeText);
        } catch (NumberFormatException e) {
            showErrorMessage("Pet age must be a valid number.");
            return;
        }

        if (!evaluatePetName(petName).equals("Good")) {
            messageLabel.setText(evaluatePetName(petName));
            return;
        }

        // Create the new pet with the default color 'brown'
        Pet newPet;
        switch (petType.toLowerCase()) {
            case "dog":
                newPet = new Dog(petName, petAge);
                break;
            case "cat":
                newPet = new Cat(petName, petAge);
                break;
            case "bird":
                newPet = new Bird(petName, petAge);
                break;
            case "fish":
                newPet = new Fish(petName, petAge);
                break;
            default:
                showErrorMessage("Invalid pet type selected.");
                return;
        }

        // Set default color to "Brown"
        newPet.setColour("Brown");

        try {
            // Add pet to the database
            petManager.addPet(newPet, userId);

            // Play the create sound based on the pet type
            SoundManager.playSound(newPet.getType(), "create");

            showErrorMessage("Pet added successfully: " + newPet.getName());
            goBackToCollectionView();
        } catch (Exception e) {
            showErrorMessage("Could not add pet to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the back button press. Navigates back to the collection view.
     */
    @FXML
    private void onBack() {
        goBackToCollectionView(); // Navigate back to collection view
    }

    /**
     * Navigates back to the CollectionView.
     */
    private void goBackToCollectionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) petNameField.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Set the new scene
            stage.setTitle("Your Pets"); // Set the window title
        } catch (IOException e) {
            showErrorMessage("Error: Could not load the Collection view."); // Error for loading view
            e.printStackTrace();
        }
    }

    /**
     * Displays an error message in the label for 3 seconds.
     *
     * @param message the error message to display
     */
    private void showErrorMessage(String message) {
        messageLabel.setText(message); // Set error message
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set error message style
        PauseTransition pause = new PauseTransition(Duration.seconds(3)); // Create a pause transition for 3 seconds
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play(); // Start the pause transition
    }
}

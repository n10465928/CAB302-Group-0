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
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;

public class PetCreationController {

    private PetManager petManager;
    int userId = UserSession.getInstance().getUserId();

    @FXML
    private TextField petNameField;

    @FXML
    private ComboBox<String> petTypeComboBox;

    @FXML
    private TextField petAgeField; // Make sure this matches your FXML

    @FXML
    private Label messageLabel; // Label for showing error messages

    public PetCreationController() {
        petManager = new PetManager(new SqlitePetDAO());
    }

    @FXML
    public void initialize() {
        petTypeComboBox.getItems().addAll("Dog", "Cat", "Bird", "Fish");
    }

    @FXML
    private void onCreatePet() {
        // Get values from the fields
        String petName = petNameField.getText();
        String petType = petTypeComboBox.getValue();
        String petAgeText = petAgeField.getText();

        // Validate fields
        if (petName == null || petName.isEmpty() || petType == null || petType.isEmpty() || petAgeText == null || petAgeText.isEmpty()) {
            showErrorMessage("All fields must be filled out.");
            return; // Stop if validation fails
        }

        // Convert age to integer
        int petAge;
        try {
            petAge = Integer.parseInt(petAgeText);
        } catch (NumberFormatException e) {
            showErrorMessage("Pet age must be a valid number.");
            return;
        }

        // Create new Pet object using the custom constructor
        Pet newPet = new Pet(petName, petType, petAge); // Assuming "Black" as a default color

        // Add pet to database
        try {
            petManager.addPet(newPet, userId); // Add the new pet to the database
            showErrorMessage("Pet added successfully: " + newPet.GetName()); // Use custom GetName() method
            goBackToCollectionView(); // Go back to collection view
        } catch (Exception e) {
            showErrorMessage("Could not add pet to the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBack() {
        goBackToCollectionView();
    }

    // Navigate back to CollectionView
    private void goBackToCollectionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) petNameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Your Pets");
        } catch (IOException e) {
            showErrorMessage("Error: Could not load the Collection view.");
            e.printStackTrace();
        }
    }

    // Show an error message in the label for 3 seconds
    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set error message style
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play();
    }

}


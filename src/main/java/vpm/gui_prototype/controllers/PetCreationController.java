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

    /**
     * Evaluates if the pets name meets the conditions:
     *      -range of 2 to 12 characters
     *      -no special characters
     * @param petName the users username input string
     * @return String message
     */
    public String evaluatePetName(String petName) {
        if (petName.length() < 2 || petName.length() > 12) {
            return "Your pets name must be from 2 to 12 characters long";
        }
        for (int i = 0; i < petName.length(); i ++) {
            char c = petName.charAt(i);
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ') {
                return "Your pets name must not contain special characters";
            }
        }
        return "Good";
    }

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

        try {
            petManager.addPet(newPet, userId);
            showErrorMessage("Pet added successfully: " + newPet.getName());
            goBackToCollectionView();
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


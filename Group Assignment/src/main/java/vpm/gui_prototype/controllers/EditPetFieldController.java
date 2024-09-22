package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

public class EditPetFieldController {

    @FXML
    private Label fieldNameLabel;

    @FXML
    private TextField fieldValueTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Pet currentPet;

    private String field;

    private PetManager petManager;

    private Runnable onSaveCallback; // Callback to be executed on save

    private final int MAX_NAME_LENGTH = 20;
    private final int MAX_AGE_LENGTH = 3; // Maximum 3 digits for age
    private final int MAX_COLOUR_LENGTH = 15;
    private final int MAX_PERSONALITY_LENGTH = 30;

    @FXML
    public void initialize() {
        petManager = new PetManager(new vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO());
        addTextLimiter(fieldValueTextField, MAX_NAME_LENGTH);
    }

    public void setPetAndField(Pet pet, String field) {
        this.currentPet = pet;
        this.field = field;

        switch (field.toLowerCase()) {
            case "name":
                fieldNameLabel.setText("Edit Pet Name");
                fieldValueTextField.setText(currentPet.GetName());
                addTextLimiter(fieldValueTextField, MAX_NAME_LENGTH);
                break;
            case "age":
                fieldNameLabel.setText("Edit Pet Age");
                fieldValueTextField.setText(currentPet.GetAge().toString());
                addTextLimiter(fieldValueTextField, MAX_AGE_LENGTH);
                break;
            case "colour":
                fieldNameLabel.setText("Edit Pet Colour");
                fieldValueTextField.setText(currentPet.GetColour());
                addTextLimiter(fieldValueTextField, MAX_COLOUR_LENGTH);
                break;
            case "personality":
                fieldNameLabel.setText("Edit Pet Personality");
                fieldValueTextField.setText(currentPet.GetPersonality());
                addTextLimiter(fieldValueTextField, MAX_PERSONALITY_LENGTH);
                break;
            default:
                fieldNameLabel.setText("Unknown Field");
                fieldValueTextField.setDisable(true);
                saveButton.setDisable(true);
        }
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    // Method to limit the characters in a TextField
    private void addTextLimiter(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    @FXML
    private void onSavePress() {
        String newValue = fieldValueTextField.getText().trim();

        if (newValue.isEmpty()) {
            fieldNameLabel.setText("Field value cannot be empty!");
            return;
        }

        switch (field.toLowerCase()) {
            case "name":
                currentPet.SetName(newValue);
                break;
            case "age":
                try {
                    currentPet.SetAge(Integer.parseInt(newValue));
                } catch (NumberFormatException e) {
                    fieldNameLabel.setText("Age must be a number!");
                    return;
                }
                break;
            case "colour":
                currentPet.SetColour(newValue);
                break;
            case "personality":
                currentPet.SetPersonality(newValue);
                break;
            default:
                fieldNameLabel.setText("Unknown Field");
                return;
        }

        // Save the updated pet to the database
        petManager.updatePet(currentPet, UserSession.getInstance().getUserId());

        // Trigger the callback to update the interaction view
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        // Close the edit window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelPress() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}

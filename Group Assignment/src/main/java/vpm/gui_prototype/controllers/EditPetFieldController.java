package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

/**
 * Controller class to handle EditPetFieldView
 */
public class EditPetFieldController {
    //UI elements
    @FXML
    private Label fieldNameLabel;

    @FXML
    private TextField fieldValueTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label messageLabel; // Label for showing error messages

    //variables for pet
    private Pet currentPet;

    private String field;

    private PetManager petManager;

    private Runnable onSaveCallback; // Callback to be executed on save
    //constants for input constraints
    private final int MAX_NAME_LENGTH = 20;
    private final int MAX_AGE_LENGTH = 3; // Maximum 3 digits for age
    private final int MAX_COLOUR_LENGTH = 15;
    private final int MAX_PERSONALITY_LENGTH = 30;

    /**
     * initialises the Editor
     */
    @FXML
    public void initialize() {
        petManager = new PetManager(new vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO());
        addTextLimiter(fieldValueTextField, MAX_NAME_LENGTH);
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

    /**
     * Sets the pet and field to be edited
     * @param pet a pet object to be edited
     * @param field the field of the detail to be edited of the pet
     */
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

    /**
     * call back to return to previous screen when saving
     * @param onSaveCallback callback function
     */
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

    /**
     * Save button press function, save the fields value to the database and update interaction view
     */
    @FXML
    private void onSavePress() {
        String newValue = fieldValueTextField.getText().trim();
        String validationMessage = "Good";

        if (newValue.isEmpty()) {
            fieldNameLabel.setText("Field value cannot be empty!");
            return;
        }

        switch (field.toLowerCase()) {
            case "name":
                validationMessage = evaluatePetName(newValue);
                if (!validationMessage.equals("Good")) {
                    messageLabel.setText(validationMessage);
                    return; // Exit the method if the name is invalid
                }
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

    /**
     * Function for cancel button, closes the edit window
     */
    @FXML
    private void onCancelPress() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}

package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox; // Import ComboBox
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

/**
 * Controller class to handle the EditPetFieldView.
 * Manages the editing of pet attributes such as name, age, colour, and personality.
 */
public class EditPetFieldController {

    // UI elements
    @FXML
    private Label fieldNameLabel; // Label for the field name

    @FXML
    private TextField fieldValueTextField; // Text field for entering the new value

    @FXML
    private ComboBox<String> colourComboBox; // ComboBox for color selection

    @FXML
    private Button saveButton; // Button to save changes

    @FXML
    private Button cancelButton; // Button to cancel editing

    @FXML
    private Label messageLabel; // Label for showing error messages

    // Variables for the pet being edited
    private Pet currentPet; // The pet object being edited
    private String field; // The field of the pet to edit
    private PetManager petManager; // Manager for pet-related database operations
    private Runnable onSaveCallback; // Callback to be executed after saving

    // Constants for input constraints
    private final int MAX_NAME_LENGTH = 20; // Maximum length for pet name
    private final int MAX_AGE_LENGTH = 3; // Maximum digits for age
    private final int MAX_PERSONALITY_LENGTH = 30; // Maximum length for personality

    /**
     * Initializes the controller.
     * Sets up the pet manager and initializes the color ComboBox.
     */
    @FXML
    public void initialize() {
        petManager = new PetManager(new vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO());
        addTextLimiter(fieldValueTextField, MAX_NAME_LENGTH);

        // Initialize color ComboBox with options
        colourComboBox.getItems().addAll("Orange", "Black", "Grey", "White");
    }

    /**
     * Evaluates if the pet's name meets the specified conditions:
     * - Length of 2 to 12 characters
     * - No special characters
     *
     * @param petName the user's input string for the pet's name
     * @return a message indicating the validation result
     */
    public String evaluatePetName(String petName) {
        if (petName.length() < 2 || petName.length() > 12) {
            return "Your pet's name must be between 2 and 12 characters long.";
        }
        for (int i = 0; i < petName.length(); i++) {
            char c = petName.charAt(i);
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ') {
                return "Your pet's name must not contain special characters.";
            }
        }
        return "Good";
    }

    /**
     * Sets the pet and the specific field to be edited.
     *
     * @param pet   the pet object to be edited
     * @param field the field of the pet's detail to be edited
     */
    public void setPetAndField(Pet pet, String field) {
        this.currentPet = pet;
        this.field = field;

        // Update UI elements based on the field being edited
        switch (field.toLowerCase()) {
            case "name":
                fieldNameLabel.setText("Edit Pet Name");
                fieldValueTextField.setText(currentPet.getName());
                addTextLimiter(fieldValueTextField, MAX_NAME_LENGTH);
                fieldValueTextField.setVisible(true);
                colourComboBox.setVisible(false);
                break;
            case "age":
                fieldNameLabel.setText("Edit Pet Age");
                fieldValueTextField.setText(currentPet.getAge().toString());
                addTextLimiter(fieldValueTextField, MAX_AGE_LENGTH);
                fieldValueTextField.setVisible(true);
                colourComboBox.setVisible(false);
                break;
            case "colour":
                fieldNameLabel.setText("Edit Pet Colour");
                colourComboBox.setValue(currentPet.getColour());
                fieldValueTextField.setVisible(false);
                colourComboBox.setVisible(true);
                break;
            case "personality":
                fieldNameLabel.setText("Edit Pet Personality");
                fieldValueTextField.setText(currentPet.getPersonality());
                addTextLimiter(fieldValueTextField, MAX_PERSONALITY_LENGTH);
                fieldValueTextField.setVisible(true);
                colourComboBox.setVisible(false);
                break;
            default:
                fieldNameLabel.setText("Unknown Field");
                fieldValueTextField.setDisable(true); // Disable text field for unknown fields
                colourComboBox.setDisable(true); // Disable ComboBox
                saveButton.setDisable(true); // Disable save button
        }
    }

    /**
     * Sets the callback function to be executed upon saving.
     *
     * @param onSaveCallback the callback function to execute
     */
    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }

    /**
     * Method to limit the number of characters in a TextField.
     *
     * @param textField the TextField to limit
     * @param maxLength the maximum allowed length
     */
    private void addTextLimiter(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(oldValue); // Revert to old value if limit exceeded
            }
        });
    }

    /**
     * Handles the save button press event.
     * Validates and saves the new field value to the database, then updates the interaction view.
     */
    @FXML
    private void onSavePress() {
        String validationMessage = "Good";

        // Check for empty input and validate based on the field
        if (field.equalsIgnoreCase("name")) {
            String newValue = fieldValueTextField.getText().trim();
            if (newValue.isEmpty()) {
                fieldNameLabel.setText("Field value cannot be empty!");
                return;
            }
            validationMessage = evaluatePetName(newValue);
            if (!validationMessage.equals("Good")) {
                messageLabel.setText(validationMessage);
                return; // Exit if the name is invalid
            }
            currentPet.setName(newValue);
        } else if (field.equalsIgnoreCase("age")) {
            String newValue = fieldValueTextField.getText().trim();
            if (newValue.isEmpty()) {
                fieldNameLabel.setText("Field value cannot be empty!");
                return;
            }
            try {
                currentPet.setAge(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                fieldNameLabel.setText("Age must be a number!");
                return;
            }
        } else if (field.equalsIgnoreCase("colour")) {
            String newValue = colourComboBox.getValue();
            if (newValue == null) {
                fieldNameLabel.setText("Please select a colour!");
                return;
            }
            currentPet.setColour(newValue);
        } else if (field.equalsIgnoreCase("personality")) {
            String newValue = fieldValueTextField.getText().trim();
            if (newValue.isEmpty()) {
                fieldNameLabel.setText("Field value cannot be empty!");
                return;
            }
            currentPet.setPersonality(newValue);
        } else {
            fieldNameLabel.setText("Unknown Field");
            return;
        }

        // Save the updated pet to the database
        petManager.updatePet(currentPet, UserSession.getInstance().getUserId());

        // Trigger the callback to refresh the interaction view
        if (onSaveCallback != null) {
            onSaveCallback.run();
        }

        // Close the edit window
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the cancel button press event.
     * Closes the edit window without saving changes.
     */
    @FXML
    private void onCancelPress() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
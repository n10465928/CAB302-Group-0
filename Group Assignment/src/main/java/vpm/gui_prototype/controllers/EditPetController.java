package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;

import java.io.IOException;

public class EditPetController {

    private Pet currentPet;
    private PetManager petManager;
    private String editDetail;
    int userId = UserSession.getInstance().getUserId();

    @FXML
    private Label petNameLabel;
    @FXML
    private Label detailLabel; // Label to display which detail is being edited
    @FXML
    private TextField detailField; // Text field to enter new detail value
    @FXML
    private Button saveButton; // Button to save changes
    @FXML
    private Button cancelButton; // Button to cancel editing

    public EditPetController() {
        petManager = new PetManager(new SqlitePetDAO());
    }

    // Sets the pet to be edited and updates the view
    public void setPet(Pet pet) {
        this.currentPet = pet;
        petNameLabel.setText(pet.GetName());
        updateDetailField();
    }

    // Set which detail is being edited
    public void setEditDetail(String editDetail) {
        this.editDetail = editDetail;
        detailLabel.setText("Edit " + capitalizeFirstLetter(editDetail)); // Display which detail is being edited
        updateDetailField(); // Update the text field with current value
    }

    // Capitalize the first letter of a string (helper method)
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Update the text field with the current value of the detail being edited
    private void updateDetailField() {
        if (currentPet == null || editDetail == null) return;

        switch (editDetail.toLowerCase()) {
            case "name":
                detailField.setText(currentPet.GetName());
                break;
            case "type":
                detailField.setText(currentPet.GetType());
                break;
            case "colour":
                detailField.setText(currentPet.GetColour());
                break;
            case "personality":
                detailField.setText(currentPet.GetPersonality());
                break;
            case "customtrait":
                detailField.setText(currentPet.GetCustomTrait());
                break;
        }
    }

    // Save the new value entered by the user
    @FXML
    private void onSave() {
        if (currentPet == null || editDetail == null) return;

        String newValue = detailField.getText();

        // Update the pet's detail based on which detail is being edited
        switch (editDetail.toLowerCase()) {
            case "name":
                currentPet.SetName(newValue);
                break;
            case "type":
                currentPet.SetType(newValue);
                break;
            case "colour":
                currentPet.SetColour(newValue);
                break;
            case "personality":
                currentPet.SetPersonality(newValue);
                break;
            case "customtrait":
                currentPet.SetCustomTrait(newValue);
                break;
        }

        // Update the pet in the database
        petManager.updatePet(currentPet, userId);

        // Navigate back to the Pet Interaction view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetInteractionView.fxml"));
            Scene scene = new Scene(loader.load());

            PetInteractionController petInteractionController = loader.getController();
            petInteractionController.setPet(currentPet); // Pass the updated pet to the interaction screen

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Interaction");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cancel editing and go back to the Pet Interaction view
    @FXML
    private void onCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetInteractionView.fxml"));
            Scene scene = new Scene(loader.load());

            PetInteractionController petInteractionController = loader.getController();
            petInteractionController.setPet(currentPet); // Pass the pet to the interaction screen

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Interaction");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

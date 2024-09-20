package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;

public class PetManagementController {

    private PetManager petManager;
    int userId = UserSession.getInstance().getUserId();

    @FXML
    private Label petNameLabel;

    @FXML
    private ComboBox<String> petColorComboBox;

    @FXML
    private ComboBox<String> petPersonalityComboBox;

    @FXML
    private TextField customTraitField;

    public PetManagementController() {
        petManager = new PetManager(new SqlitePetDAO());
    }

    private Pet currentPet;

    public void setPet(Pet pet) {
        this.currentPet = pet;
        petNameLabel.setText(pet.GetName());

        // Pre-populate customization options (for demonstration)
        petColorComboBox.getItems().addAll("Red", "Green", "Blue", "Yellow");
        petPersonalityComboBox.getItems().addAll("Friendly", "Aggressive", "Playful", "Shy");
    }

    // Navigate back to CollectionView
    private void goBackToCollectionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) petNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Your Pets");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveCustomizations() {
        String selectedColor = petColorComboBox.getValue();
        String selectedPersonality = petPersonalityComboBox.getValue();
        String customTrait = customTraitField.getText();

        if (selectedColor != null) {
            System.out.println("Pet Color: " + selectedColor);
        }

        if (selectedPersonality != null) {
            System.out.println("Pet Personality: " + selectedPersonality);
        }

        if (customTrait != null && !customTrait.isEmpty()) {
            System.out.println("Custom Trait: " + customTrait);
        }

        goBackToCollectionView();

        // Implement logic to save these customizations to the pet
    }

    @FXML
    private void onDelete() {
        petManager.deletePet(currentPet, userId);
        goBackToCollectionView();
    }
}

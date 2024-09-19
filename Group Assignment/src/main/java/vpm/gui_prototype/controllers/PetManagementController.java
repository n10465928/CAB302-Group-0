package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

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

        // Implement logic to save these customizations to the pet
    }
}

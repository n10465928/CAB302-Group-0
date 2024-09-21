package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    private TextField petAgeField;


    @FXML
    private ComboBox<String> petTypeComboBox;
    public PetCreationController() {
        petManager = new PetManager(new SqlitePetDAO());
    }

    @FXML
    public void initialize() {
        petTypeComboBox.getItems().addAll("Dog", "Cat", "Bird", "Fish");
    }

    @FXML
    private void onCreatePet() {
        String petName = petNameField.getText();
        String petType = petTypeComboBox.getValue();
        Integer petAge = Integer.valueOf(petAgeField.getText());

        if (petName != null && petType != null && petAge != null) {
            Pet newPet = new Pet(petName, petType, petAge);
            petManager.addPet(newPet, userId); // Add the new pet to the service

            goBackToCollectionView();
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
            e.printStackTrace();
        }
    }
}

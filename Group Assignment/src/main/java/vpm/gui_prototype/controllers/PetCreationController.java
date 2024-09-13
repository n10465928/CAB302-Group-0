package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.services.PetService;

import java.io.IOException;

public class PetCreationController {

    @FXML
    private TextField petNameField;

    @FXML
    private ComboBox<String> petTypeComboBox;

    private PetService petService;

    // Set the PetService instance
    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    @FXML
    public void initialize() {
        petTypeComboBox.getItems().addAll("Dog", "Cat", "Bird", "Fish");
    }

    @FXML
    private void onCreatePet() {
        String petName = petNameField.getText();
        String petType = petTypeComboBox.getValue();

        if (petName != null && petType != null) {
            Pet newPet = new Pet(petName, 0, petType);
            petService.addPet(newPet); // Add the new pet to the service

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

            CollectionController collectionController = loader.getController();
            collectionController.setPetService(petService); // Pass back the service

            Stage stage = (Stage) petNameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Your Pets");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

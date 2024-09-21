package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.FoodStuff.Food;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;

public class PetInteractionController {

    private PetManager petManager;
    int userId = UserSession.getInstance().getUserId();

    @FXML
    private Label PetName;
    @FXML
    private Label HappinessField;
    @FXML
    private Label HungerField;
    @FXML
    private Label CleanField;

    private Pet currentPet;


    public void setPet(Pet pet) {
        currentPet = pet;
        PetName.setText(pet.GetName());
        HappinessField.setText(String.valueOf(pet.GetHappiness()));
        HungerField.setText(String.valueOf(pet.GetFoodSatisfaction()));
        if(pet.GetIsDirty()){
            CleanField.setText("Dirty");
        }
        else{CleanField.setText("Clean");}
    }

    @FXML
    private void onPlay(){
        currentPet.IncreaseHappiness(1f);
        HappinessField.setText(String.valueOf(currentPet.GetHappiness()));
    }

    @FXML
    private void onFeed(){
        currentPet.Feed(1f);
        HungerField.setText(String.valueOf(currentPet.GetFoodSatisfaction()));
    }

    @FXML
    private void onClean(){
        currentPet.Clean();
        CleanField.setText("Clean");
    }

    @FXML
    private void onInteraction(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetManagementView.fxml"));
            Scene scene = new Scene(loader.load());

            PetManagementController petManagementController = loader.getController();
            petManagementController.setPet(currentPet); // Pass the pet to the management screen

            Stage stage = (Stage) HappinessField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manage Pet: " + currentPet.GetName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

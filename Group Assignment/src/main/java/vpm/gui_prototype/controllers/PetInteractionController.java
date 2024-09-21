package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.FoodStuff.Food;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;
import java.io.InputStream;

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
    @FXML
    private Label picturePetName;
    @FXML
    private Label picturePetType;
    @FXML
    private ImageView image;
    @FXML
    private Label petName;
    @FXML
    private Label petType;
    @FXML
    private Label petColour;
    @FXML
    private Label petPersonality;


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
        picturePetName.setText(currentPet.GetName());
        picturePetType.setText(currentPet.GetType());
        setImage();
        petName.setText(currentPet.GetName());
        petType.setText(currentPet.GetType());
        petColour.setText(currentPet.GetColour());
        petPersonality.setText(currentPet.GetPersonality());
    }

    // Get the image path for the corresponding pet type
    private String getPetImagePath(String petType) {
        switch (petType.toLowerCase()) {
            case "cat":
                return "/assets/cat.png";
            case "dog":
                return "/assets/dog.png";
            case "fish":
                return "/assets/fish.png";
            case "bird":
                return "/assets/bird.png";
            default:
                return null; // No specific image path for this type
        }
    }

    private void setImage(){
        String imagePath = getPetImagePath(currentPet.GetType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));
            } else {
                // Log an error if the image is not found
                System.err.println("Error: Pet image not found for path: " + imagePath);
            }
        } else {
            // Set a default image if the pet type is not recognized
            image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));
        }
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

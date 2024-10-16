package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class DeleteConfirmationController {

    private Pet pet;  // The pet to be deleted
    private PetManager petManager;
    private IUserDAO userDAO;
    int userId = UserSession.getInstance().getUserId(); // Manager for pet-related database operations
    private Consumer<Boolean> onConfirmationCallback;

    @FXML
    private Label confirmationMessage;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public void setPet(Pet pet) {
        this.pet = pet;
        confirmationMessage.setText("Are you sure you want to delete " + pet.getName() + "?");
    }

    @FXML
    private void onConfirm() {
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(true); // Notify that the user confirmed
        }
        // Close the dialog
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(false); // Notify that the user cancelled
        }
        // Close the dialog
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setOnConfirmationCallback(Consumer<Boolean> callback) {
        this.onConfirmationCallback = callback;
    }

    public DeleteConfirmationController() {
        petManager = new PetManager(new SqlitePetDAO());
        this.userDAO = new SqliteUserDAO();
    }
}

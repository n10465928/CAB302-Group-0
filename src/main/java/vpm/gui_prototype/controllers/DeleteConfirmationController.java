package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.function.Consumer;

/**
 * Controller for the Delete Confirmation dialog.
 * This class handles the user interactions for confirming or cancelling the deletion of a pet.
 */
public class DeleteConfirmationController {

    // Callback to notify when the user confirms or cancels the action
    private Consumer<Boolean> onConfirmationCallback;

    @FXML
    private Label confirmationMessage; // Label to display the confirmation message
    @FXML
    private Button confirmButton; // Button to confirm the deletion
    @FXML
    private Button cancelButton; // Button to cancel the deletion

    /**
     * Sets the pet to be deleted and updates the confirmation message.
     *
     * @param pet the pet to be deleted
     */
    public void setPet(Pet pet) {
        // Update the confirmation message with the pet's name
        confirmationMessage.setText("Are you sure you want to delete " + pet.getName() + "?");
    }

    /**
     * Handles the confirm button click event.
     * Notifies the callback of the user's confirmation and closes the dialog.
     */
    @FXML
    private void onConfirm() {
        // Notify the callback that the user confirmed the deletion
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(true);
        }
        // Close the dialog
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the cancel button click event.
     * Notifies the callback of the user's cancellation and closes the dialog.
     */
    @FXML
    private void onCancel() {
        // Notify the callback that the user cancelled the deletion
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(false);
        }
        // Close the dialog
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the callback to be invoked on confirmation or cancellation.
     *
     * @param callback the callback function to handle confirmation response
     */
    public void setOnConfirmationCallback(Consumer<Boolean> callback) {
        this.onConfirmationCallback = callback;
    }
}

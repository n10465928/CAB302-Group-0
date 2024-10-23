package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.function.Consumer;

/**
 * Controller for the Logout Confirmation dialog.
 * This class handles user interactions for confirming or cancelling a logout action.
 */
public class LogoutConfirmationController {

    // Callback to notify when the user confirms or cancels the logout action
    private Consumer<Boolean> onConfirmationCallback;

    @FXML
    private Label confirmationMessage; // Label to display the confirmation message
    @FXML
    private Button confirmButton; // Button to confirm the logout
    @FXML
    private Button cancelButton; // Button to cancel the logout

    /**
     * Sets the message to be displayed in the confirmation dialog.
     *
     * @param message the confirmation message to display
     */
    public void setMessage(String message) {
        // Update the confirmation message with the provided text
        confirmationMessage.setText(message);
    }

    /**
     * Handles the confirm button click event.
     * Notifies the callback of the user's confirmation and closes the dialog.
     */
    @FXML
    private void onConfirm() {
        // Notify the callback that the user confirmed the logout
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
        // Notify the callback that the user cancelled the logout
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

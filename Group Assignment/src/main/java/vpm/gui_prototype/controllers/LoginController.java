package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.services.LoginService;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorMessageLabel;

    private IUserDAO userDAO;

    public LoginController() {
        userDAO = new SqliteUserDAO();
    }

    //return true if fields have inputs, otherwise false
    public boolean validInputs(String username, String password){
        if(!username.isEmpty() && !password.isEmpty()){
            return true;
        }
        //ToDO: make message appear in interface
        else{System.out.println("Lack of credentials"); return false;}
    }

    // return true if the credentials match, false otherwise
    public boolean verifyUser(String username, String password) {
        return userDAO.verifyUser(username, password);
    }

    //go to login screen
    @FXML
    void onLoginPress() {
        // Verify user's credentials first
        String username = usernameField.getText();
        String password = passwordField.getText();

        //verify user inputs are valid
        if(!validInputs(username, password)){
            return;
        }

        if (!verifyUser(username, password)) {
            // ToDo: Make UI effects to alert users that they don't have correct credentials
            // System.out.println(username + password + " Not right!");
            errorMessageLabel.setText("Username or password is incorrect");
            return;
        }

        try {
            LoginService loginservice = new LoginService();
            loginservice.login(userDAO.getUserID(username, password));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRegisterPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/RegisterView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

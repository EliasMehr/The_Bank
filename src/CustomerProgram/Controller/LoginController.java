package CustomerProgram.Controller;

import CustomerProgram.CustomerViews;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {


    @FXML
    private TextField personalIdField;
    @FXML
    private PasswordField pinField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    @FXML
    private void attemptLogin(ActionEvent actionEvent) {

        //TODO VALIDATE PIN CODE
        //if(isSuccessfulLogin)
        CustomerViews.changeScene(CustomerViews.View.ACCOUNT_OVERVIEW);

    }
}

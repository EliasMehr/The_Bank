package CustomerProgram.Controller;

import CustomerProgram.CustomerViews;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class LoginController {


    @FXML
    private PasswordField passwordField;

    @FXML
    private void attemptLogin(ActionEvent actionEvent) {

        //TODO VALIDATE PIN CODE
        //if(isSuccessfulLogin)
        CustomerViews.changeScene(CustomerViews.View.ACCOUNT_OVERVIEW);

    }
}

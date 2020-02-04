package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import CustomerProgram.CustomerViews;
import Repository.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    @FXML
    private void attemptLogin(ActionEvent actionEvent) {

        try {
            if (usernameField.getLength() == 0 && passwordField.getLength() == 0) {
                CustomerMain.showErrorMessage("Fälten kan inte vara tomma", "Ingen data inmatad");
            } else {
                CustomerMain.customerIdentity = CustomerRepository.login(usernameField.getText(), Integer.parseInt(passwordField.getText()));

                if (CustomerMain.customerIdentity == 0) {
                    CustomerMain.showErrorMessage("Felaktig PIN eller personnummer", "Felaktig inloggning");
                } else {
                    CustomerMain.showInformationMessage("Skickar vidare till portalen", "Inloggning accepterad");
                    CustomerViews.changeScene(CustomerViews.View.ACCOUNT_OVERVIEW);
                }
            }

        } catch (NumberFormatException e) {
            CustomerMain.showErrorMessage("PIN får endast innehålla siffror", "Numberformat exception");
        }
    }
}

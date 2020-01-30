package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AccountOverviewController {
    @FXML
    private TableView accountsTable;
    @FXML
    private TableColumn accountNumCol;
    @FXML
    private TableColumn accountNameCol;
    @FXML
    private TableColumn accountAmountCol;
    @FXML
    private TableColumn accountInterestCol;
    @FXML
    private TableView loansTable;
    @FXML
    private TableColumn loanNameCol;
    @FXML
    private TableColumn loanAmountCol;
    @FXML
    private TableColumn monthlyPaymentCol;
    @FXML
    private TableColumn loanInterestCol;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;



    @FXML
    private PasswordField pinField;
    @FXML
    private PasswordField pinConfirmationField;
    private Customer temporaryTestCustomer;


    public void initialize(){
        temporaryTestCustomer = new Customer();
        temporaryTestCustomer.setFirstName("Luke");
        temporaryTestCustomer.setLastName("Skywalker");
        temporaryTestCustomer.setPersonalID("19281012-2893");
        temporaryTestCustomer.setPin(1234);

        updateCustomerFields();


    }

    private void updateCustomerFields() {
        firstNameField.setText(temporaryTestCustomer.getFirstName());
        lastNameField.setText(temporaryTestCustomer.getLastName());
    }


    @FXML
    private void saveCustomerProfile(ActionEvent actionEvent) {
        final String firstName = firstNameField.getText();
        final String lastName = lastNameField.getText();

        if(temporaryTestCustomer.getFirstName().equals(firstName)
                                    && temporaryTestCustomer.getLastName().equals(lastName)){
            CustomerMain.showErrorMessage("Nothing has changed", "Cannot update profile");
        }
        else{

            //TODO set values in database
            //If successful change in database, do the following
            temporaryTestCustomer.setFirstName(firstName);
            temporaryTestCustomer.setLastName(lastName);
            updateCustomerFields();
        }
    }

    @FXML
    private void changeCustomerPIN(ActionEvent actionEvent) {
        final String pinErrorTitle = "Could not change PIN";

        if(pinField.getText().isEmpty() || pinConfirmationField.getText().isEmpty()){
            CustomerMain.showErrorMessage("PIN Field cannot be blank!", pinErrorTitle);
        }
        else if(!pinField.getText().equals(pinConfirmationField.getText())){
            CustomerMain.showErrorMessage("PIN fields do not match!", pinErrorTitle);
        }
        else{
            try {
                temporaryTestCustomer.setPin(Integer.parseInt(pinField.getText()));
                CustomerMain.showInformationMessage("PIN Code successfully changed", "Yay!");
                //TODO CHANGE PIN in database
            } catch (NumberFormatException e) {
               CustomerMain.showErrorMessage("PIN must consist of only numbers", pinErrorTitle);
            }
        }


    }

    @FXML
    private void seeAccountHistory(ActionEvent actionEvent) {
    }

    @FXML
    private void seeLoanHistory(ActionEvent actionEvent) {
    }
}

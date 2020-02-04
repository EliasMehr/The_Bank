package AdminProgram.Controller;

import AdminProgram.AdminMain;
import Model.Customer;
import Repository.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CustomerOverviewController {
    @FXML
    private TextField searchCustomerField;
    @FXML
    private TextField personalNumberField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField pinField;

    public void addCustomer(MouseEvent mouseEvent) {
    }

    public void remove(ActionEvent actionEvent) {
    }

    @FXML
    private void createNewAccountOrLoan(ActionEvent actionEvent) {
    }

    @FXML
    private void findCustomer() {
        Customer customer = CustomerRepository.getCustomerByPersonalNumber(searchCustomerField.getText());
        if(customer == null)
            AdminMain.showErrorMessage("Personnumret finns inte i databasen", "Kunde inte hämta personuppgifter");
        else{
            AdminMain.customerIdentity = customer.getCustomerId();
            firstNameField.setText(customer.getFirstName());
            lastNameField.setText(customer.getLastName());
            pinField.setText(String.valueOf(customer.getPin()));
            personalNumberField.setText(customer.getPersonalNumber());
            
            populateAccountsOverview(customer);
            populateAccountsOverview(customer);
        }
    }

    private void populateAccountsOverview(Customer customer) {
    }

    @FXML
    private void updatePersonalInformation(ActionEvent actionEvent) {
    }

    @FXML
    private void deleteCustomer(ActionEvent actionEvent) {
    }
}

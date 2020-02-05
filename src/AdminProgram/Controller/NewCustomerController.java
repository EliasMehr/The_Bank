package AdminProgram.Controller;

import AdminProgram.AdminMain;
import AdminProgram.AdminViews;
import Model.Customer;
import Repository.AccountRepository;
import Repository.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Random;

public class NewCustomerController {


    public TextField customerName;
    public TextField customerLastName;
    public TextField customerPersonID;
    public TextField customerAccountNumb;
    public PasswordField customerPIN;
    public Label customer_label;


    public void initialize() {


        customerPIN.setText(generateAccountPIN());
        customerAccountNumb.setText(generateAccountNumber());

    }


    public String generateAccountNumber(){
        Random genRand = new Random();
        int accountNumber = genRand.nextInt(100000000) + 99999999;
        return String.valueOf(accountNumber);
    }

    public String generateAccountPIN() {
        Random genRand = new Random();
        int pinCode = genRand.nextInt(1001) + 999;
        return String.valueOf(pinCode);
    }

    public void createNewCustomer(ActionEvent actionEvent) {
        int pinCode = Integer.parseInt(customerPIN.getText());
        int accountNumber = Integer.parseInt(customerAccountNumb.getText());
        String firstname = customerName.getText();
        String lastname = customerLastName.getText();
        String personID = customerPersonID.getText();


        if (customerName.getText().isEmpty() && customerLastName.getText().isEmpty() && customerPersonID.getText().isEmpty()) {
            AdminMain.showErrorMessage("Inga fält får vara tomma", "Felaktig inmatning");
        } else {
            CustomerRepository.addCustomer(firstname, lastname, personID, pinCode);
            Customer customer = CustomerRepository.getCustomerByPersonalNumber(customerPersonID.getText());
            AccountRepository.createAccount(customer.getCustomerId(), 0, accountNumber, "Person-konto");

            AdminMain.customerIdentity = customer.getCustomerId();

            customer_label.setText("Ny kund - REGISTRERAD!");
        }
    }

    public void returnToPortal(ActionEvent actionEvent) {
        AdminViews.changeScene(AdminViews.View.CUSTOMER_OVERVIEW);
    }
}

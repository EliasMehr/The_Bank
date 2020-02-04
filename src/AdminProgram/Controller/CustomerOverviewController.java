package AdminProgram.Controller;

import AdminProgram.AdminMain;
import Model.Account;
import Model.Customer;
import Model.Loan;
import Model.Transaction;
import Repository.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.Date;

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
    @FXML
    private TableView<Account> accountsOverview;
    @FXML
    private TableColumn<Account, Integer> accountNumberCol;
    @FXML
    private TableColumn<Account, String> accountAmountCol;
    @FXML
    private TableColumn<Account, String> accountInterestCol;
    @FXML
    private TableView<Loan> loansOverview;
    @FXML
    private TableColumn<Loan, String> loanAmountCol;
    @FXML
    private TableColumn<Loan, String> loanRemainingCol;
    @FXML
    private TableColumn<Loan, String> loanMortgageCol;
    @FXML
    private TableColumn<Loan, String> loanInterestCol;
    @FXML
    private TableColumn<Loan, String> loanPaymentPlanCol;
    @FXML
    private TableView<Transaction> transactionHistory;
    @FXML
    private TableColumn<Transaction, LocalDateTime> transactionDateCol;
    @FXML
    private TableColumn<Transaction, String> transactionTypeCol;
    @FXML
    private TableColumn<Transaction, String> transactionAmountCol;
    @FXML
    private TableColumn<Transaction, String> transactionAccountCol;
    @FXML
    private DatePicker fromDateSelector;
    @FXML
    private DatePicker toDateSelector;

    private Customer currentCustomer;

    @FXML
    private void createNewAccountOrLoan(ActionEvent actionEvent) {
    }

    @FXML
    private void findCustomer() {
         currentCustomer = CustomerRepository.getCustomerByPersonalNumber(searchCustomerField.getText());
        if(currentCustomer == null)
            AdminMain.showErrorMessage("Personnumret finns inte i databasen", "Kunde inte hämta personuppgifter");
        else{
            AdminMain.customerIdentity = currentCustomer.getCustomerId();
            firstNameField.setText(currentCustomer.getFirstName());
            lastNameField.setText(currentCustomer.getLastName());
            pinField.setText(String.valueOf(currentCustomer.getPin()));
            personalNumberField.setText(currentCustomer.getPersonalNumber());
            
            populateAccountsOverview(currentCustomer);
            populateAccountsOverview(currentCustomer);
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

    @FXML
    private void saveLoanChanges(ActionEvent actionEvent) {
    }

    @FXML
    private void depositMoney(ActionEvent actionEvent) {
    }

    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
    }

    @FXML
    private void changeAccountInterest(ActionEvent actionEvent) {
    }

    @FXML
    private void removeCustomer(ActionEvent actionEvent) {
        //Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION, "Ta Bort " + selection + " ?", ButtonType.OK, ButtonType.CANCEL);
        //CustomerRepository.deleteCustomer()
    }

    @FXML
    private void addCustomer(MouseEvent mouseEvent) {
    }
}

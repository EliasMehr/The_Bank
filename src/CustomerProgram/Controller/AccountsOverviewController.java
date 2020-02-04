package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import Model.Account;
import Model.Customer;
import Model.Loan;
import Model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.Date;

public class AccountsOverviewController {

    @FXML
    private TableView<Account> accountsOverview;
    @FXML
    private TableColumn<Account, Integer> accountCol;
    @FXML
    private TableColumn<Account, String> accountTypeCol;
    @FXML
    private TableColumn<Account, Double> accountAmountCol;
    @FXML
    private TableColumn<Account, Double> accountInterestCol;
    @FXML
    private TableView<Loan> loansOverview;
    @FXML
    private TableColumn<Loan, String> loanTypeCol;
    @FXML
    private TableColumn<Loan, Double> loanAmountCol;
    @FXML
    private TableColumn loanMortgageCol;
    @FXML
    private TableColumn loanInterestCol;
    @FXML
    private TableColumn loanPaymentPlanCol;
    @FXML
    private TableView<Transaction> transactionHistory;
    @FXML
    private TableColumn<Transaction, Date> transactionDateCol;
    @FXML
    private TableColumn<Transaction, String> transactionTypeCol;
    @FXML
    private TableColumn<Transaction, Double> transactionAmountCol;
    @FXML
    private TableColumn<Transaction, Integer> transactionAccountCol;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TextField withdrawalAmountField;
    @FXML
    private ComboBox<Account> withdrawalAccountSelector;
    @FXML
    private Label customerNameLabel;

    private Customer customer;


    public void initialize(){
    customer = new Customer(); //TODO Get customer from database
    customerNameLabel.setText("Välkommen till The Bank, " + customer.getFirstName() + " " + customer.getLastName());
    populateAccountsOverview();
    populateLoansOverview();

    }

    private void populateLoansOverview() {
    }

    private void populateAccountsOverview() {
    }


    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
    }

    @FXML
    private void validateAmountInput(KeyEvent keyEvent) {
    }
}

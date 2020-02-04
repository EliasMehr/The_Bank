package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import Model.Account;
import Model.Customer;
import Model.Loan;
import Model.Transaction;
import Repository.AccountRepository;
import Repository.CustomerRepository;
import Repository.LoanRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.text.NumberFormat;
import java.util.Date;

public class AccountsOverviewController {

    @FXML
    private TableView<Account> accountsOverview;
    @FXML
    private TableColumn<Account, Integer> accountNumCol;
    @FXML
    private TableColumn<Account, String> accountAmountCol;
    @FXML
    private TableColumn<Account, String> accountInterestCol;
    @FXML
    private TableView<Loan> loansOverview;
    @FXML
    private TableColumn<Loan, String> loanAmountCol;
    @FXML
    private TableColumn<Loan, String> loanMortgageCol;
    @FXML
    private TableColumn<Loan, String> loanInterestCol;
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


    public void initialize() {
        customer = CustomerRepository.getCustomerById(CustomerMain.customerIdentity);
        customerNameLabel.setText("Välkommen till The Bank, " + customer.getFirstName() + " " + customer.getLastName());

        populateAccountsOverview();
        populateLoansOverview();
        populateWithdrawalAccountSelector();
    }

    private void populateWithdrawalAccountSelector() {
        withdrawalAccountSelector.setItems(FXCollections.observableList(customer.getAccounts()));
    }

    private void populateLoansOverview() {
        LoanRepository.getLoans(customer);
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        loanAmountCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getAmount())));
        loanInterestCol.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getInterestRate() + "%"));
        loanMortgageCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getMonthlyPayment()) + "/Mån"));

        loansOverview.setItems(FXCollections.observableList(customer.getLoans()));
    }

    private void populateAccountsOverview() {
        AccountRepository.getAccounts(customer);
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        accountNumCol.setCellValueFactory(account -> new SimpleIntegerProperty(account.getValue().getAccountNumber()).asObject());
        accountAmountCol.setCellValueFactory(account -> new SimpleStringProperty(currency.format(account.getValue().getAmount())));
        accountInterestCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getInterestRate() + "%"));

        accountsOverview.setItems(FXCollections.observableList(customer.getAccounts()));
    }


    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {

    }

    @FXML
    private void validateAmountInput(KeyEvent keyEvent) {

    }
}

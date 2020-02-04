package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import CustomerProgram.CustomerViews;
import Model.Account;
import Model.Customer;
import Model.Loan;
import Model.Transaction;
import Repository.AccountRepository;
import Repository.CustomerRepository;
import Repository.LoanRepository;
import Repository.TransactionRepository;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class AccountsOverviewController {

    public TableColumn<Account, String> accountTypeCol;
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
    private TableColumn<Loan, String> loanPaymentPlanCol;
    @FXML
    private TableView<Transaction> transactionHistory;
    @FXML
    private TableColumn<Transaction, LocalDate> transactionDateCol;
    @FXML
    private TableColumn<Transaction, String> transactionTypeCol;
    @FXML
    private TableColumn<Transaction, String> transactionAmountCol;
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
        populateTransactionHistory();

    }

    private void populateTransactionHistory() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        TransactionRepository.recentMonthTransactions(customer.getAccounts().get(0));

        transactionAmountCol.setCellValueFactory(transaction -> new SimpleStringProperty(currency.format(transaction.getValue().getAmount())));
        transactionDateCol.setCellValueFactory(transaction -> new SimpleObjectProperty(transaction.getValue().getDate().toLocalDate()));
        transactionAccountCol.setCellValueFactory(transaction -> new SimpleIntegerProperty(customer.getAccounts().get(0).getAccountNumber()).asObject());

        transactionHistory.setItems(FXCollections.observableList(customer.getAccounts().get(0).getTransactions()));
    }

    private void populateWithdrawalAccountSelector() {
        withdrawalAccountSelector.setItems(FXCollections.observableList(customer.getAccounts()));
    }

    private void populateLoansOverview() {
        LoanRepository.getLoans(customer);
        DecimalFormat formatDoubles = new DecimalFormat("#.#");
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        loanAmountCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getAmount())));
        loanInterestCol.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getInterestRate() + "%"));
        loanMortgageCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getMortagePlan()) + "/Mån"));
        loanPaymentPlanCol.setCellValueFactory(loan -> new SimpleStringProperty(formatDoubles.format(loan.getValue().getMonthlyPayment()) + " År"));
        loansOverview.setItems(FXCollections.observableList(customer.getLoans()));
    }

    private void populateAccountsOverview() {
        AccountRepository.getAccounts(customer);
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        accountNumCol.setCellValueFactory(account -> new SimpleIntegerProperty(account.getValue().getAccountNumber()).asObject());
        accountAmountCol.setCellValueFactory(account -> new SimpleStringProperty(currency.format(account.getValue().getAmount())));
        accountInterestCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getInterestRate() + "%"));
        accountTypeCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getAccountType()));


        accountsOverview.setItems(FXCollections.observableList(customer.getAccounts()));
    }


    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
        int withdrawalAmmount = Integer.parseInt(withdrawalAmountField.getText());

        if (withdrawalAmountField.getText().isEmpty()) {
            CustomerMain.showErrorMessage("Ogiltig inmatning", "Felaktig inmatning");
        } else if (withdrawalAmmount <= 0) {
            CustomerMain.showErrorMessage("Ogiltig inmatning", "Du kan inte mata in ett negativt tal");
        } else {
            AccountRepository.withdraw(withdrawalAccountSelector.getSelectionModel().getSelectedItem().getAccountId(), withdrawalAmmount);
            populateAccountsOverview();
            populateWithdrawalAccountSelector();
        }
    }

    @FXML
    private void validateAmountInput(KeyEvent keyEvent) {

    }

    public void signOut(ActionEvent actionEvent) {
        CustomerViews.changeScene(CustomerViews.View.LOGIN);
    }
}

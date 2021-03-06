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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.stream.Collectors;

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
    private TableColumn<Transaction, LocalDate> transactionDateCol;
    @FXML
    private TableColumn<Transaction, String> transactionTypeCol;
    @FXML
    private TableColumn<Transaction, String> transactionAmountCol;
    @FXML
    private TableColumn<Transaction, Integer> transactionAccountCol;
    @FXML
    private DatePicker fromDateSelector;
    @FXML
    private DatePicker toDateSelector;
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
        fromDateSelector.setValue(LocalDate.now().minusMonths(1));
        toDateSelector.setValue(LocalDate.now());
        populateAccountsOverview();
        populateLoansOverview();
        populateWithdrawalAccountSelector();
        populateTransactionHistory(customer.getAccounts().get(0));

    }

    private void populateTransactionHistory(Account currentAccount) {
        transactionHistory.getItems().clear();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        TransactionRepository.getTransactions(currentAccount, fromDateSelector.getValue(), toDateSelector.getValue());


        transactionAmountCol.setCellValueFactory(transaction -> new SimpleStringProperty(currency.format(transaction.getValue().getAmount())));
        transactionDateCol.setCellValueFactory(transaction -> new SimpleObjectProperty(transaction.getValue().getDate()));
        transactionAccountCol.setCellValueFactory(transaction -> new SimpleIntegerProperty(currentAccount.getAccountNumber()).asObject());
        transactionTypeCol.setCellValueFactory(transaction -> {
            var amount = new SimpleDoubleProperty(transaction.getValue().getAmount());

            return Bindings.when(amount.greaterThan(0))
                    .then("INSÄTTNING").otherwise("UTTAG");
        });

        transactionHistory.setItems(FXCollections.observableList(currentAccount.getTransactions()));
    }

    private void populateWithdrawalAccountSelector() {
        withdrawalAccountSelector.setItems(FXCollections.observableList(customer.getAccounts()));
    }

    private void populateLoansOverview() {
        LoanRepository.getLoans(customer);
        DecimalFormat formatDoubles = new DecimalFormat("#.#");
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        loanAmountCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getAmount())));
        loanRemainingCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getRemainingAmount())));
        loanInterestCol.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getInterestRate() + "%"));
        loanMortgageCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getMortagePlan()) + "/Mån"));
        loanPaymentPlanCol.setCellValueFactory(loan -> new SimpleStringProperty(formatDoubles.format(loan.getValue().getMonthlyPayment()) + " År"));
        loansOverview.setItems(FXCollections.observableList(customer.getLoans()));
    }

    private void populateAccountsOverview() {
        AccountRepository.getAccounts(customer);
        System.out.println(customer.getAccounts().size());
        customer.getAccounts().forEach(account -> System.out.println(account.getAccountNumber() + ", " + account.getAccountType()));
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        accountNumCol.setCellValueFactory(account -> new SimpleIntegerProperty(account.getValue().getAccountNumber()).asObject());
        accountAmountCol.setCellValueFactory(account -> new SimpleStringProperty(currency.format(account.getValue().getAmount())));
        accountInterestCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getInterestRate() + "%"));
        accountTypeCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getAccountType()));


        accountsOverview.setItems(FXCollections.observableList(customer.getAccounts()));
    }


    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
        if (withdrawalAmountField.getText().isEmpty()) {
            CustomerMain.showErrorMessage("Uttagsbelopp måste anges", "Felaktig inmatning");
        } else if (withdrawalAccountSelector.getSelectionModel().isEmpty()){
            CustomerMain.showErrorMessage("Du måste välja uttagskonto", "Konto ej valt");
        } else {
            int withdrawalAmmount;
            try {
                withdrawalAmmount = Integer.parseInt(withdrawalAmountField.getText());
            } catch (NumberFormatException e) {
                CustomerMain.showErrorMessage("Endast siffror i uttagsbeloppet", "Felaktig inmatning");
                return;
            }

            boolean isSuccessfulWithdrawal = AccountRepository.withdraw(withdrawalAccountSelector.getSelectionModel().getSelectedItem().getAccountId(), withdrawalAmmount);

            if(isSuccessfulWithdrawal){
                CustomerMain.showInformationMessage(withdrawalAmmount + " uttaget från kontot", "Uttag genomfört");
                withdrawalAccountSelector.setPromptText("Välj Konto");
                populateAccountsOverview();
                populateWithdrawalAccountSelector();
            }
        }
    }

    public void signOut(ActionEvent actionEvent) {
        CustomerViews.changeScene(CustomerViews.View.LOGIN);
    }

    @FXML
    private void updateTransactionHistory() {
        if(!accountsOverview.getSelectionModel().isEmpty())
            populateTransactionHistory(accountsOverview.getSelectionModel().getSelectedItem());
    }
}

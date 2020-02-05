package AdminProgram.Controller;

import AdminProgram.AdminMain;
import CustomerProgram.CustomerMain;
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
import javafx.scene.input.MouseEvent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.stream.Collectors;

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
    private TableColumn<Account, Integer> accountNumCol;
    @FXML
    private TableColumn<Account, String> accountTypeCol;
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
    private TableColumn<Transaction, Integer> transactionAccountCol;
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
            refreshCustomerInformationFields();

            fromDateSelector.setValue(LocalDate.now().minusMonths(1));
            toDateSelector.setValue(LocalDate.now());

            populateAccountsOverview();
            populateLoansOverview();
            populateTransactionHistory();
        }
    }

    private void refreshCustomerInformationFields() {
        AdminMain.customerIdentity = currentCustomer.getCustomerId();
        firstNameField.setText(currentCustomer.getFirstName());
        lastNameField.setText(currentCustomer.getLastName());
        pinField.setText(String.valueOf(currentCustomer.getPin()));
        personalNumberField.setText(currentCustomer.getPersonalNumber());
    }


    private void populateAccountsOverview() {
        AccountRepository.getAccounts(currentCustomer);
        System.out.println(currentCustomer.getAccounts().size());
        currentCustomer.getAccounts().forEach(account -> System.out.println(account.getAccountNumber() + ", " + account.getAccountType()));
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        accountNumCol.setCellValueFactory(account -> new SimpleIntegerProperty(account.getValue().getAccountNumber()).asObject());
        accountAmountCol.setCellValueFactory(account -> new SimpleStringProperty(currency.format(account.getValue().getAmount())));
        accountInterestCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getInterestRate() + "%"));
        accountTypeCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getAccountType()));


        accountsOverview.setItems(FXCollections.observableList(currentCustomer.getAccounts()));
    }

    private void populateLoansOverview() {
        LoanRepository.getLoans(currentCustomer);
        DecimalFormat formatDoubles = new DecimalFormat("#.#");
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        loanAmountCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getAmount())));
        loanRemainingCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getRemainingAmount())));
        loanInterestCol.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getInterestRate() + "%"));
        loanMortgageCol.setCellValueFactory(loan -> new SimpleStringProperty(currency.format(loan.getValue().getMortagePlan()) + "/Mån"));
        loanPaymentPlanCol.setCellValueFactory(loan -> new SimpleStringProperty(formatDoubles.format(loan.getValue().getMonthlyPayment()) + " År"));
        loansOverview.setItems(FXCollections.observableList(currentCustomer.getLoans()));
    }

    private void populateTransactionHistory() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        Account currentAccount = currentCustomer.getAccounts().get(0);
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

    @FXML
    private void updatePersonalInformation(ActionEvent actionEvent) {
        try {
            int newPin = Integer.parseInt(pinField.getText());

            currentCustomer.setFirstName(firstNameField.getText());
            currentCustomer.setLastName(lastNameField.getText());
            currentCustomer.setPersonalNumber(personalNumberField.getText());
            currentCustomer.setPin(newPin);
            boolean isChangedCustomerInfo = CustomerRepository.changePersonalInfo(currentCustomer, currentCustomer.getFirstName(), currentCustomer.getLastName(), currentCustomer.getPersonalNumber(), currentCustomer.getPin());
            if(isChangedCustomerInfo){
                refreshCustomerInformationFields();
                CustomerMain.showInformationMessage("Personliga uppgifter uppdaterat för " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName(), "Tadaa!!");
            }
            else {
                AdminMain.showErrorMessage("Kunde inte uppdatera kunduppgifter", "Något gick fel");
            }
        } catch (NumberFormatException e) {
            AdminMain.showErrorMessage("PIN får endast innehålla siffror!", "Ogiltig PIN-kod");
        }
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

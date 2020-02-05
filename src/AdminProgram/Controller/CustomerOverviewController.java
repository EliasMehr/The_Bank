package AdminProgram.Controller;

import AdminProgram.AdminMain;
import AdminProgram.AdminViews;
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

    private Customer customer;

    @FXML
    private void createNewAccountOrLoan(ActionEvent actionEvent) {
    }

    @FXML
    private void findCustomer() {
         customer = CustomerRepository.getCustomerByPersonalNumber(searchCustomerField.getText());
        if(customer == null)
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
        AdminMain.customerIdentity = customer.getCustomerId();
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        pinField.setText(String.valueOf(customer.getPin()));
        personalNumberField.setText(customer.getPersonalNumber());
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

    private void populateTransactionHistory() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        Account currentAccount = customer.getAccounts().get(0);
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

            customer.setFirstName(firstNameField.getText());
            customer.setLastName(lastNameField.getText());
            customer.setPersonalNumber(personalNumberField.getText());
            customer.setPin(newPin);
            boolean isChangedCustomerInfo = CustomerRepository.changePersonalInfo(customer, customer.getFirstName(), customer.getLastName(), customer.getPersonalNumber(), customer.getPin());
            if(isChangedCustomerInfo){
                refreshCustomerInformationFields();
                CustomerMain.showInformationMessage("Personliga uppgifter uppdaterat för " + customer.getFirstName() + " " + customer.getLastName(), "Tadaa!!");
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
        if (AdminMain.customerIdentity != 0) {
            Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION, "Ta bort kund " + customer.getFirstName() + " "+ customer.getLastName() + "?", ButtonType.OK, ButtonType.CANCEL);
            deleteCustomerAlert.showAndWait();
            if(deleteCustomerAlert.getResult() == ButtonType.OK){
                CustomerRepository.deleteCustomer(customer.getCustomerId());
                AdminMain.showInformationMessage("Kund borttagen från registret", "Farväl kära kund");
                AdminMain.customerIdentity = 0;
                AdminViews.changeScene(AdminViews.View.CUSTOMER_OVERVIEW);
            }
        }

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
    private void deleteAccount(ActionEvent actionEvent) {

    }

    @FXML
    private void addCustomer(MouseEvent mouseEvent) {
        AdminViews.changeScene(AdminViews.View.NEW_CUSTOMER);
    }
}

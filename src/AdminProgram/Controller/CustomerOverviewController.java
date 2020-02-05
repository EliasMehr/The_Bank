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
import java.util.stream.IntStream;

public class CustomerOverviewController {
    @FXML
    private TextField accountInterestField;
    @FXML
    private TextField accountAmountField;
    @FXML
    private TextField loanInterestField;
    @FXML
    private TextField loanPaymentPlanField;
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

    public void initialize(){
        if(AdminMain.customerIdentity != 0){
            customer = CustomerRepository.getCustomerById(AdminMain.customerIdentity);
            loadCustomerData();
        }
    }


    @FXML
    private void createNewAccountOrLoan(ActionEvent actionEvent) {
        AdminViews.changeScene(AdminViews.View.NEW_ACCOUNT_OR_LOAN);
    }


    @FXML
    private void findCustomer() {
        customer = CustomerRepository.getCustomerByPersonalNumber(searchCustomerField.getText());
        if (customer == null)
            AdminMain.showErrorMessage("Personnumret finns inte i databasen", "Kunde inte hämta personuppgifter");
        else {
            loadCustomerData();
        }
    }

    private void loadCustomerData() {
        refreshCustomerInformationFields();

        fromDateSelector.setValue(LocalDate.now().minusMonths(1));
        toDateSelector.setValue(LocalDate.now());

        populateAccountsOverview();
        populateLoansOverview();
        if(customer.getAccounts().size() != 0){
            populateTransactionHistory(customer.getAccounts().get(0));
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
        accountsOverview.getItems().clear();
        AccountRepository.getAccounts(customer);
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        accountNumCol.setCellValueFactory(account -> new SimpleIntegerProperty(account.getValue().getAccountNumber()).asObject());
        accountAmountCol.setCellValueFactory(account -> new SimpleStringProperty(currency.format(account.getValue().getAmount())));
        accountInterestCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getInterestRate() + "%"));
        accountTypeCol.setCellValueFactory(account -> new SimpleStringProperty(account.getValue().getAccountType()));


        accountsOverview.setItems(FXCollections.observableList(customer.getAccounts()));
    }

    private void populateLoansOverview() {
        loansOverview.getItems().clear();
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

    private void populateTransactionHistory(Account account) {
        transactionHistory.getItems().clear();
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        TransactionRepository.getTransactions(account, fromDateSelector.getValue(), toDateSelector.getValue());


        transactionAmountCol.setCellValueFactory(transaction -> new SimpleStringProperty(currency.format(transaction.getValue().getAmount())));
        transactionDateCol.setCellValueFactory(transaction -> new SimpleObjectProperty(transaction.getValue().getDate()));
        transactionAccountCol.setCellValueFactory(transaction -> new SimpleIntegerProperty(account.getAccountNumber()).asObject());
        transactionTypeCol.setCellValueFactory(transaction -> {
            var amount = new SimpleDoubleProperty(transaction.getValue().getAmount());

            return Bindings.when(amount.greaterThan(0))
                    .then("INSÄTTNING").otherwise("UTTAG");
        });

        transactionHistory.setItems(FXCollections.observableList(account.getTransactions()));
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
            if (isChangedCustomerInfo) {
                refreshCustomerInformationFields();
                CustomerMain.showInformationMessage("Personliga uppgifter uppdaterat för " + customer.getFirstName() + " " + customer.getLastName(), "Tadaa!!");
            } else {
                AdminMain.showErrorMessage("Kunde inte uppdatera kunduppgifter", "Något gick fel");
            }
        } catch (NumberFormatException e) {
            AdminMain.showErrorMessage("PIN får endast innehålla siffror!", "Ogiltig PIN-kod");
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent actionEvent) {
        if (AdminMain.customerIdentity != 0) {
            Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION, "Ta bort kund " + customer.getFirstName() + " " + customer.getLastName() + "?", ButtonType.OK, ButtonType.CANCEL);
            deleteCustomerAlert.showAndWait();
            if (deleteCustomerAlert.getResult() == ButtonType.OK) {
                CustomerRepository.deleteCustomer(customer.getCustomerId());
                AdminMain.showInformationMessage("Kund borttagen från registret", "Farväl kära kund");
                AdminMain.customerIdentity = 0;
                AdminViews.changeScene(AdminViews.View.CUSTOMER_OVERVIEW);
            }
        }

    }


    @FXML
    private void depositMoney(ActionEvent actionEvent) {
        if (accountAmountField.getText().isEmpty()) {
            CustomerMain.showErrorMessage("Insättningsbelopp måste anges", "Felaktig inmatning");
        } else if (accountsOverview.getSelectionModel().isEmpty()) {
            CustomerMain.showErrorMessage("Du måste välja uttagskonto", "Konto ej valt");
        } else {
            int depositAmount;
            try {
                depositAmount = Integer.parseInt(accountAmountField.getText());
            } catch (NumberFormatException e) {
                CustomerMain.showErrorMessage("Endast siffror i insättningsbeloppet", "Felaktig inmatning");
                return;
            }

            Account account = accountsOverview.getSelectionModel().getSelectedItem();

            boolean isSuccessfulDeposit = AccountRepository.deposit(account.getAccountId(), depositAmount);

            if (isSuccessfulDeposit) {
                CustomerMain.showInformationMessage(depositAmount + " insatt på kontot", "Insättning genomförd");
                populateAccountsOverview();
            }
        }
    }

    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
        if (accountAmountField.getText().isEmpty()) {
            CustomerMain.showErrorMessage("Uttagsbelopp måste anges", "Felaktig inmatning");
        } else if (accountsOverview.getSelectionModel().isEmpty()) {
            CustomerMain.showErrorMessage("Du måste välja uttagskonto", "Konto ej valt");
        } else {
            int withdrawalAmount;
            try {
                withdrawalAmount = Integer.parseInt(accountAmountField.getText());
            } catch (NumberFormatException e) {
                CustomerMain.showErrorMessage("Endast siffror i uttagsbeloppet", "Felaktig inmatning");
                return;
            }

            Account account = accountsOverview.getSelectionModel().getSelectedItem();
            boolean isSuccessfulWithdrawal = AccountRepository.withdraw(account.getAccountId(), withdrawalAmount);

            if (isSuccessfulWithdrawal) {
                CustomerMain.showInformationMessage(withdrawalAmount + " uttaget från kontot", "Uttag genomfört");
                populateAccountsOverview();
            }
        }
    }

    @FXML
    private void changeAccountInterest(ActionEvent actionEvent) {
        if (accountsOverview.getSelectionModel().isEmpty()) {
            AdminMain.showErrorMessage("Du måste välja ett konto", "Kunde inte ändra räntan");
        } else {
            try {
                double newInterestRate = Double.parseDouble(accountInterestField.getText());
                Account account = accountsOverview.getSelectionModel().getSelectedItem();

                boolean isChangedInterestRate = AccountRepository.changeAccountInterest(account.getAccountId(), newInterestRate);

                if (isChangedInterestRate) {
                    AdminMain.showInformationMessage("Räntan ändrad", "Allt gick bra");
                    populateAccountsOverview();
                } else {
                    AdminMain.showErrorMessage("Något gick fel", "Kunde inte ändra räntan");
                }
            } catch (NumberFormatException e) {
                AdminMain.showErrorMessage("Ränta måste anges som decimaltal", "Kunde inte ändra räntan");
            }
        }

    }

    @FXML
    private void deleteAccount(ActionEvent actionEvent) {
        if (accountsOverview.getSelectionModel().isEmpty()) {
            AdminMain.showErrorMessage("Du måste välja ett konto", "Kan inte ta bort ett konto som inte är valt");
        } else {
            Account account = accountsOverview.getSelectionModel().getSelectedItem();
            if (AdminMain.customerIdentity != 0) {
                Alert deleteAccountAlert = new Alert(Alert.AlertType.CONFIRMATION, "Ta bort konto " + account.getAccountNumber() + "?", ButtonType.OK, ButtonType.CANCEL);
                deleteAccountAlert.showAndWait();
                if (deleteAccountAlert.getResult() == ButtonType.OK) {
                    boolean isDeletedAccount = AccountRepository.deleteAccount(account.getAccountId());

                    if (isDeletedAccount) {
                        AdminMain.showInformationMessage("Konto avslutat", "Bye bye konto");
                        populateAccountsOverview();
                        transactionHistory.getItems().clear();
                    } else {
                        AdminMain.showErrorMessage("Något gick fel", "Kunde inte ta bort kontot");
                    }
                }

            }
        }
    }

    @FXML
    private void addCustomer(MouseEvent mouseEvent) {
        AdminViews.changeScene(AdminViews.View.NEW_CUSTOMER);
    }

    @FXML
    private void changeLoanInterest(ActionEvent actionEvent) {
        if (loansOverview.getSelectionModel().isEmpty()) {
            AdminMain.showErrorMessage("Du måste välja ett lån", "Kunde inte ändra räntan");
        } else {
            try {
                double newInterestRate = Double.parseDouble(loanInterestField.getText());
                Loan loan = loansOverview.getSelectionModel().getSelectedItem();

                boolean isChangedInterestRate = LoanRepository.changeInterestRate(loan.getLoanId(), newInterestRate);

                if (isChangedInterestRate) {
                    AdminMain.showInformationMessage("Räntan ändrad", "Allt gick bra");
                    populateLoansOverview();
                } else {
                    AdminMain.showErrorMessage("Något gick fel", "Kunde inte ändra räntan");
                }
            } catch (NumberFormatException e) {
                AdminMain.showErrorMessage("Ränta måste anges som decimaltal", "Kunde inte ändra räntan");
            }
        }
    }

    @FXML
    private void changeLoanPaymentPlan(ActionEvent actionEvent) {
        if (loansOverview.getSelectionModel().isEmpty()) {
            AdminMain.showErrorMessage("Du måste välja ett lån", "Kunde inte ändra betalplan");
        } else {
            try {
                double newPaymentPlan = Integer.parseInt(loanPaymentPlanField.getText());
                Loan loan = loansOverview.getSelectionModel().getSelectedItem();

                boolean isChangedPaymentPlan = LoanRepository.changePaymentPlan(loan.getLoanId(), newPaymentPlan);

                if (isChangedPaymentPlan) {
                    AdminMain.showInformationMessage("Betalplan ändrad", "Allt gick bra");
                    populateLoansOverview();
                } else {
                    AdminMain.showErrorMessage("Något gick fel", "Kunde inte ändra betalplan");
                }
            } catch (NumberFormatException e) {
                AdminMain.showErrorMessage("Betalplan måste anges som en siffra", "Kunde inte ändra betalplan");
            }
        }
    }

    public void updateTransactionHistory(ActionEvent actionEvent) {
        if(!accountsOverview.getSelectionModel().isEmpty())
            populateTransactionHistory(accountsOverview.getSelectionModel().getSelectedItem());
    }
}

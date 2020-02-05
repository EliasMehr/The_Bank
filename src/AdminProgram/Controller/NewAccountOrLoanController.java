package AdminProgram.Controller;

import AdminProgram.AdminMain;
import AdminProgram.AdminViews;
import Model.Customer;
import Repository.AccountRepository;
import Repository.CustomerRepository;
import Repository.LoanRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Random;

public class NewAccountOrLoanController {

    public ComboBox<Double> paymentPlan_Selector;
    public ComboBox<Double> interestRate_Selector;
    public ComboBox<String> account_Selector;
    public ComboBox<Double> accountInterest_selector;
    public TextField amount_txt;
    public TextField depositMoney_txt;
    public Label manageCustomer_label;
    private Customer customer;

    public void initialize() {
        populatePaymentSelector();
        populateInterestSelector();
        populateAccountTypeSelector();
        populateAccountInterestRate();

        customer = CustomerRepository.getCustomerById(AdminMain.customerIdentity);
        manageCustomer_label.setText("Hanterar: " + customer.getFirstName() + " " + customer.getLastName());
    }

    private void populateAccountInterestRate() {
        accountInterest_selector.getItems().add(1.0);
        accountInterest_selector.getItems().add(2.0);
        accountInterest_selector.getItems().add(3.0);
        accountInterest_selector.getItems().add(4.0);
        accountInterest_selector.getItems().add(5.0);
        accountInterest_selector.getItems().add(6.0);
        accountInterest_selector.getItems().add(7.0);
        accountInterest_selector.getItems().add(8.0);
        accountInterest_selector.getItems().add(9.0);
        accountInterest_selector.getItems().add(10.0);
    }

    private void populateAccountTypeSelector() {
        account_Selector.getItems().add("Person-konto");
        account_Selector.getItems().add("Sparkonto");
        account_Selector.getItems().add("KF-Konto");
        account_Selector.getItems().add("ISK-Konto");
    }

    private void populateInterestSelector() {
        interestRate_Selector.getItems().add(1.5);
        interestRate_Selector.getItems().add(2.2);
        interestRate_Selector.getItems().add(3.5);
        interestRate_Selector.getItems().add(4.5);
        interestRate_Selector.getItems().add(5.5);
        interestRate_Selector.getItems().add(6.2);
        interestRate_Selector.getItems().add(7.2);
        interestRate_Selector.getItems().add(8.2);
    }

    private void populatePaymentSelector() {
        paymentPlan_Selector.getItems().add(1.0);
        paymentPlan_Selector.getItems().add(2.0);
        paymentPlan_Selector.getItems().add(3.0);
        paymentPlan_Selector.getItems().add(4.0);
        paymentPlan_Selector.getItems().add(5.0);
        paymentPlan_Selector.getItems().add(6.0);
        paymentPlan_Selector.getItems().add(7.0);
        paymentPlan_Selector.getItems().add(8.0);
        paymentPlan_Selector.getItems().add(9.0);
        paymentPlan_Selector.getItems().add(10.0);
    }

    public String generateAccountNumber() {
        Random genRand = new Random();
        int accountNumber = genRand.nextInt(100000000) + 99999999;
        return String.valueOf(accountNumber);
    }

    public void processLoanApplyment(ActionEvent actionEvent) {
        if (amount_txt.getText().isEmpty()) {
            AdminMain.showErrorMessage("Du måste hantera alla fält", "ERROR");
        } else {
            double amount = Double.parseDouble(amount_txt.getText());
            double paymentPlan = paymentPlan_Selector.getSelectionModel().getSelectedItem();
            double interestValue = interestRate_Selector.getSelectionModel().getSelectedItem();

            boolean isSuccessful = LoanRepository.createLoan(amount, interestValue, paymentPlan, customer.getCustomerId());

            if (isSuccessful) {
                AdminMain.showInformationMessage("LÅN BEVILJAT", "GODKÄNT");
            } else {
                AdminMain.showErrorMessage("LÅNET BLEV EJ BEVILJAT", "EJ GODKÄNT");
            }
        }
    }

    public void createNewAccount(ActionEvent actionEvent) {
        if (depositMoney_txt.getText().isEmpty()) {
            AdminMain.showErrorMessage("Måste hantera alla fält", "ERROR");
        } else {
            double amount = Double.parseDouble(depositMoney_txt.getText());
            double interestRate = accountInterest_selector.getSelectionModel().getSelectedItem();
            String accountType = account_Selector.getSelectionModel().getSelectedItem();
            int accountNumber = Integer.parseInt(generateAccountNumber());

            boolean isSuccessful = AccountRepository.createAccount(customer.getCustomerId(), amount, accountNumber, accountType);

            if (isSuccessful) {
                AdminMain.showInformationMessage("Nytt konto skapat", "Genomfört");
            }
        }
    }

    public void returnToPortal(ActionEvent actionEvent) {
        AdminViews.changeScene(AdminViews.View.CUSTOMER_OVERVIEW);
    }
}

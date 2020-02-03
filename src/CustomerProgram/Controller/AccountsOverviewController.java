package CustomerProgram.Controller;

import CustomerProgram.CustomerMain;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class AccountsOverviewController {

    @FXML
    private TableView accountsOverview;
    @FXML
    private TableColumn accountCol;
    @FXML
    private TableColumn accountTypeCol;
    @FXML
    private TableColumn accountAmountCol;
    @FXML
    private TableColumn accountInterestCol;
    @FXML
    private TableView loansOverview;
    @FXML
    private TableColumn loanTypeCol;
    @FXML
    private TableColumn loanAmountCol;
    @FXML
    private TableColumn loanMortgageCol;
    @FXML
    private TableColumn loanInterestCol;
    @FXML
    private TableColumn loanPaymentPlanCol;
    @FXML
    private TableView transactionHistory;
    @FXML
    private TableColumn transactionDateCol;
    @FXML
    private TableColumn transactionTypeCol;
    @FXML
    private TableColumn transactionAmountCol;
    @FXML
    private TableColumn transactionAccountCol;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TextField withdrawalAmountField;
    @FXML
    private ComboBox withdrawalAccountSelector;
    @FXML
    private Label customerNameLabel;


    public void initialize(){


    }


    @FXML
    private void withdrawMoney(ActionEvent actionEvent) {
    }

    @FXML
    private void validateAmountInput(KeyEvent keyEvent) {
    }
}

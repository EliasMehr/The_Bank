package AdminProgram.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Random;

public class NewCustomerController {


    public TextField customerName;
    public TextField customerLastName;
    public TextField customerPersonID;
    public TextField customerAccountNumb;
    public PasswordField customerPIN;

    public void initialize() {

    }

    public static String generateAccountNumber(long prefix) {
        long accountDigits = (long) (Math.random() * 1000000000000L);
        long generateAccountNumber = prefix * 1000000000000L;
        long generate16digitsWithPrefix = generateAccountNumber + accountDigits;
        return String.valueOf(generate16digitsWithPrefix);
    }

    public int generateAccountPIN() {
        Random genRand = new Random();
        int pinCode = genRand.nextInt(1001) + 999;
        return pinCode;
    }

    public void createNewCustomer(ActionEvent actionEvent) {

    }
}

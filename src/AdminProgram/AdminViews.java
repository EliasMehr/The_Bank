package AdminProgram;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViews {

    public static Stage stage;

    public enum View {
        CUSTOMER_OVERVIEW,
        NEW_ACCOUNT_OR_LOAN,
        NEW_CUSTOMER
    }


    public static void changeScene(AdminProgram.AdminViews.View view) {

        String fxmlName;


        switch (view) {

            case CUSTOMER_OVERVIEW:
                fxmlName = "CustomerOverView";
                break;
            case NEW_ACCOUNT_OR_LOAN:
                fxmlName = "NewAccountOrLoan";
                break;
            case NEW_CUSTOMER:
                fxmlName = "NewCustomer";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view);
        }

        try {
            Parent parent = FXMLLoader.load(CustomerProgram.CustomerViews.class.getResource("../AdminProgram/View/" + fxmlName + ".fxml"));
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



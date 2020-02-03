package CustomerProgram;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerViews {

    public static Stage stage;

    public enum View {
        LOGIN,
        ACCOUNT_OVERVIEW
    }


    public static void changeScene(View view){

        String fxmlName;


        switch (view){

            case LOGIN:
                fxmlName = "Login";
                break;
            case ACCOUNT_OVERVIEW:
                fxmlName = "AccountsOverview";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view);
        }

        try {
            Parent parent = FXMLLoader.load(CustomerViews.class.getResource("../CustomerProgram/View/" + fxmlName + ".fxml"));
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

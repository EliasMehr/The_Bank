package AdminProgram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AdminMain extends Application {

    public static int customerIdentity = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(AdminMain.class.getResource("View/CustomerOverView.fxml"));
        primaryStage.setTitle("The Bank - Customer Portal");
        primaryStage.setScene(new Scene(root));
        AdminViews.stage = primaryStage;
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void showErrorMessage(String message, String title){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.show();
    }

    public static void showInformationMessage(String message, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.show();
    }
}

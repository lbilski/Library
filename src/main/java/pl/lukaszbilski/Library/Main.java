package pl.lukaszbilski.Library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.MariadbConnector;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainView.fxml"));
        primaryStage.setTitle("Biblioteka");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        MariadbConnector mariadbConnector = MariadbConnector.getInstance();

        mariadbConnector.getConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

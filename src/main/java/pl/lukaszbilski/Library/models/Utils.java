package pl.lukaszbilski.Library.models;

import javafx.scene.control.Alert;

public class Utils {
    public static void openDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

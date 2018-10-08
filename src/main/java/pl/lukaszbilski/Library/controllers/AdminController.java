package pl.lukaszbilski.Library.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pl.lukaszbilski.Library.models.User;


import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable{

    @FXML
    Label welcome;

    public void initialize(URL location, ResourceBundle resources) {
        welcome.setText("WIIITAJ ADMINIE :)");
    }
}

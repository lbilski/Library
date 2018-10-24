package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable{

    @FXML
    Label welcome;

    MainController main = new MainController();

    public void initialize(URL location, ResourceBundle resources) {
        welcome.setText("Wiiitaj: )" + main.getActiveUser().getRole());
    }
}

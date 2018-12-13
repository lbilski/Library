package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.User;


public class EditUserController {

    @FXML
    TextField login, name, surname, mail, phoneNumber;

    @FXML
    Button closeButton;
    public User activeUser;

    public void init(){
        login.setText(activeUser.getLogin());
        name.setText(activeUser.getName());
        surname.setText(activeUser.getSurname());
        mail.setText(activeUser.getMail());
        phoneNumber.setText(String.valueOf(activeUser.getPhoneNumber()));
    }

    public void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

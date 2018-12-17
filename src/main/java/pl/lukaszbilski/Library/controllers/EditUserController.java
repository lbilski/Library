package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EditUserController {

    @FXML
    TextField login, name, surname, mail, phoneNumber;

    @FXML
    Button closeButton, editMail, editPhoneNumber;

    public User activeUser;
    private Utils utils = new Utils();

    public void init(){
        login.setText(activeUser.getLogin());
        name.setText(activeUser.getName());
        surname.setText(activeUser.getSurname());
        mail.setText(activeUser.getMail());
        phoneNumber.setText(String.valueOf(activeUser.getPhoneNumber()));
    }

    public void saveToDataBase(){
        if(mail.isEditable()){
            if(utils.isEmpty(mail.getText()) || utils.regexEmail(mail.getText())){
                utils.openDialog("Błędne dane", "Niepoprawny format maila");
                mail.setTooltip(new Tooltip("Przykładowy mail to: jan@kowalski.pl"));
                return;
            }

            String query = "UPDATE user SET mail = ? WHERE user_id = ?";
            try {
                PreparedStatement statement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
                statement.setString(1, mail.getText());
                statement.setInt(2, activeUser.getUser_id());
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            activeUser.setMail(mail.getText());
            editMail.setText("Edytuj");
            mail.setEditable(false);
            utils.openDialog("Edycja danych", "Zmieniono adres mail");
        }

        if(phoneNumber.isEditable()){
            if(utils.isEmpty(phoneNumber.getText()) || utils.regexPhone(phoneNumber.getText())){
                utils.openDialog("Błędne dane", "Niepoprawny format numeru telefonu");
                phoneNumber.setTooltip(new Tooltip("Przykładowy mail to: 123456789"));
                return;
            }

            String query = "UPDATE user SET phoneNumber = ? WHERE user_id = ?";
            try {
                PreparedStatement statement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
                statement.setInt(1, Integer.parseInt(phoneNumber.getText()));
                statement.setInt(2, activeUser.getUser_id());
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            activeUser.setPhoneNumber(Integer.parseInt(phoneNumber.getText()));
            editPhoneNumber.setText("Edytuj");
            phoneNumber.setEditable(false);
            utils.openDialog("Edycja danych", "Zmieniono numer telefonu");
        }
        init();
    }

    public void close(){
        Stage stage = (Stage) closeButton.getScene().getWindow();

        stage.close();
    }

    public void editMail(){
        if(mail.isEditable()){
            mail.setEditable(false);
            mail.setText(activeUser.getMail());
            editMail.setText("Edytuj");
        }else{
            mail.setEditable(true);
            mail.setText("");
            mail.setPromptText("Podaj nowy adres mail");
            editMail.setText("Anuluj");
        }
    }

    public void editPhoneNumber(){
        if(phoneNumber.isEditable()){
            phoneNumber.setEditable(false);
            phoneNumber.setText(String.valueOf(activeUser.getPhoneNumber()));
            editPhoneNumber.setText("Edytuj");
        }else{
            phoneNumber.setEditable(true);
            phoneNumber.setText(null);
            phoneNumber.setPromptText("Podaj nowy numer telefonu");
            editPhoneNumber.setText("Anuluj");
        }
    }


}

package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.NonNull;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class MainController{
    @FXML
    TextField regLogin, regName, regSurname, regMail, regPhoneNumber, logLogin;

    @FXML
    PasswordField regPassword, regRepeatPassword, logPassword;


    private static User activeUser = new User();

    //function will start after pressing button "Zarejestruj", after validation creates new user in database;
    public void registration() {

        if(validating()) {
            String sql = "INSERT INTO user(login, name, surname, password, mail, phoneNumber, role) VALUES(?,?,?,?,?,?,?)";
            try {
                PreparedStatement statement = MariadbConnector.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, regLogin.getText());
                statement.setString(2, regName.getText());
                statement.setString(3, regSurname.getText());
                statement.setString(4, Utils.hashPassword(regPassword.getText()));
                statement.setString(5, regMail.getText());
                statement.setInt(6, Integer.parseInt(regPhoneNumber.getText()));
                statement.setString(7, "user");

                statement.execute();
                statement.close();

                Utils.openDialog("Rejestracja", "Użytkownik dodany poprawnie, \nprzejdź do sekcji logowania");
                clearFields();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //function will start after pressing button "Zaloguj", checking login, password and role in database.
    public void login(MouseEvent event) throws IOException {

        Statement statement = MariadbConnector.getInstance().getNewStatemnt();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE login = '" + logLogin.getText() + "' LIMIT 1");

            if (resultSet.next()) {
                String roleFromDB = resultSet.getString("role");
                String passwordFromDB = resultSet.getString("password");

                if (Utils.hashPassword(logPassword.getText()).equals(passwordFromDB)) {
                    if (roleFromDB.equals("admin")) {
                        activeUser.setLogin(resultSet.getString("login"));
                        activeUser.setName(resultSet.getString("name"));
                        activeUser.setSurname(resultSet.getString("surname"));
                        activeUser.setPassword(resultSet.getString("password"));
                        activeUser.setMail(resultSet.getString("mail"));
                        activeUser.setPhoneNumber(resultSet.getInt("phoneNumber"));
                        activeUser.setRole(resultSet.getString("role"));

                        Parent mainPage = FXMLLoader.load(getClass().getResource("/fxml/adminView.fxml"));
                        Scene scene = new Scene(mainPage);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.show();

                    } else if (roleFromDB.equals("user")) {
                        activeUser.setLogin(resultSet.getString("login"));
                        activeUser.setName(resultSet.getString("name"));
                        activeUser.setSurname(resultSet.getString("surname"));
                        activeUser.setPassword(resultSet.getString("password"));
                        activeUser.setMail(resultSet.getString("mail"));
                        activeUser.setPhoneNumber(resultSet.getInt("phoneNumber"));
                        activeUser.setRole(resultSet.getString("role"));

                        Parent mainPage = FXMLLoader.load(getClass().getResource("/fxml/userView.fxml"));
                        Scene scene = new Scene(mainPage);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.show();
                    }
                } else {
                    Utils.openDialog("Logowanie", "Błędne hasło, spróbuj ponownie");
                    logPassword.setText("");
                }
            } else {
                Utils.openDialog("Logowanie", "Podany użytkownik nie istnieje");
                logLogin.setText("");
                logPassword.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //function returns user who is logged.
    public User getActiveUser(){
        return activeUser;
    }

    //validaint all fields in registry form
    private boolean validating(){
        boolean result = true;
        String errorStyle = "-fx-border-color: red; -fx-opacity: 90%";
        StringBuilder info = new StringBuilder();
        clearStyle();

        //validating regLogin Field
        if(Utils.isEmpty(regLogin.getText())){
            info.append("Login powinien mieć od 3 do 25 znaków\n");
            regLogin.setStyle(errorStyle);
            regLogin.setTooltip(new Tooltip("Login powinien mieć od 3 do 25 znaków"));
            result = false;
        }else if(Utils.ifExistInDataBase(regLogin.getText()) != 0){
            info.append("Podany login jest już zajęty\n");
            regLogin.setStyle(errorStyle);
            regLogin.setTooltip(new Tooltip("Podany login jest już zajęty"));
            result = false;
        }

        //validating regName Field
        if(Utils.isEmpty(regName.getText())){
            info.append("Imię musi mieć od 3 do 25 znaków\n");
            regName.setStyle(errorStyle);
            regName.setTooltip(new Tooltip("Imię musi mieć od 3 do 25 znaków"));
            result = false;
        }

        //validating regSurname Field
        if(Utils.isEmpty(regName.getText())){
            info.append("Nazwisko musi mieć od 3 do 25 znaków\n");
            regSurname.setStyle(errorStyle);
            regSurname.setTooltip(new Tooltip("Nazwisko musi mieć od 3 do 25 znaków"));
            result = false;
        }

        //validating regMail Field
        if(Utils.isEmpty(regMail.getText()) || Utils.regexEmail(regMail.getText())){
            info.append("Wprowadzono niepoprawny format maila\n");
            regMail.setStyle(errorStyle);
            regMail.setTooltip(new Tooltip("Przykładowy mail to: jan@kowalski.pl"));
            result = false;
        }

        //validating regPhoneNumber Field
        if(Utils.isEmpty(regPhoneNumber.getText()) || Utils.regexPhone(regPhoneNumber.getText())){
            info.append("Wprowadzono niepoprawny format numeru telefonu\n");
            regPhoneNumber.setStyle(errorStyle);
            regPhoneNumber.setTooltip(new Tooltip("Przykładowy numer telefonu to: 123456789"));
            result = false;
        }

        //validating regPasswords Fields
        if(Utils.isEmpty(regPassword.getText()) || Utils.isEmpty(regRepeatPassword.getText())){
            info.append("Hasło musi mieć od 3 do 25 znaków\n");
            regPassword.setStyle(errorStyle);
            regRepeatPassword.setStyle(errorStyle);
            regPassword.setTooltip(new Tooltip("Hasło musi mieć od 3 do 25 znaków"));
            regRepeatPassword.setTooltip(new Tooltip("Hasło musi mieć od 3 do 25 znaków"));
            result = false;
        } else if (!regPassword.getText().equals(regRepeatPassword.getText())) {
            info.append("Podane hasła nie są takie same\n");
            regPassword.setStyle(errorStyle);
            regRepeatPassword.setStyle(errorStyle);
            regPassword.setTooltip(new Tooltip("Hasła muszą być takie same"));
            regRepeatPassword.setTooltip(new Tooltip("Hasła muszą być takie same"));
            result = false;
        }


        if(!result){
            Utils.openDialog("Rejestracja", info.toString());
        }

        return result;
    }

    private void clearFields(){
        regLogin.setText("");
        regName.setText("");
        regSurname.setText("");
        regMail.setText("");
        regPassword.setText("");
        regRepeatPassword.setText("");
        regPhoneNumber.setText("");
    }

    private void clearStyle(){
        regLogin.setStyle(null);
        regName.setStyle(null);
        regSurname.setStyle(null);
        regMail.setStyle(null);
        regPassword.setStyle(null);
        regRepeatPassword.setStyle(null);
        regPhoneNumber.setStyle(null);
    }
}

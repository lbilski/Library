package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.NonNull;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainController {
    @FXML
    TextField regLogin, regName, regSurname, regMail, regPhoneNumber, logLogin;

    @FXML
    PasswordField regPassword, regRepeatPassword, logPassword;

    private static User activeUser = new User();
    
    public void registration() {
        String sql = "INSERT INTO user(login, name, surname, password, mail, phoneNumber, role) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = MariadbConnector.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, regLogin.getText());
            statement.setString(2, regName.getText());
            statement.setString(3, regSurname.getText());
            statement.setString(4, regPassword.getText());
            statement.setString(5, regMail.getText());
            statement.setInt(6, Integer.parseInt(regPhoneNumber.getText()));
            statement.setString(7, "user");

            statement.execute();
            statement.close();

            Utils.openDialog("Rejestracja", "Użytkownik dodany poprawnie, \nprzejdź do sekcji logowania");
            regLogin.setText("");
            regName.setText("");
            regSurname.setText("");
            regMail.setText("");
            regPassword.setText("");
            regRepeatPassword.setText("");
            regPhoneNumber.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login(MouseEvent event) throws IOException {

        // check database for a login
        Statement statement = MariadbConnector.getInstance().getNewStatemnt();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE login = '" + logLogin.getText() + "' LIMIT 1");

            if (resultSet.next()) {
                String roleFromDB = resultSet.getString("role");
                String passwordFromDB = resultSet.getString("password");

                if (logPassword.getText().equals(passwordFromDB)) {
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

    public User getActiveUser(){
        return activeUser;
    }
}

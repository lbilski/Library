package pl.lukaszbilski.Library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditPasswordController {

    @FXML
    PasswordField currentPassword, newPassword, newPasswordRepeat;

    @FXML
    Label labelNewPassword, labelNewPasswordRepeat;

    @FXML
    Button accountCloseButton;

    private Utils utils = new Utils();
    public User activeUser;

    public void saveAndClose(){
        Stage stage = (Stage) accountCloseButton.getScene().getWindow();

        if(checkNewPasswordFields()){
            String query = "UPDATE user SET password = ? WHERE user_id = ?";
            try {
                PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
                preparedStatement.setString(1, utils.hashPassword(newPassword.getText()));
                preparedStatement.setInt(2, activeUser.getUser_id());
                preparedStatement.execute();
                preparedStatement.close();

                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void isCorrectPassword(){
        if(utils.hashPassword(currentPassword.getText()).equals(activeUser.getPassword())){
            newPassword.setDisable(false);
            newPasswordRepeat.setDisable(false);
            labelNewPassword.setDisable(false);
            labelNewPasswordRepeat.setDisable(false);
            currentPassword.setEditable(false);
        }else{
            utils.openDialog("Biblioteka", "Podano błędne hasło");
        }
    }

    private boolean checkNewPasswordFields(){
        int length = newPassword.getText().trim().length();
        boolean result = true;

        if(length < 3 || length > 25){
            utils.openDialog("Hasło", "Długość hasła powinna mieć od 3 do 25 znaków");
            result = false;
        }else if(!newPassword.getText().equals(newPasswordRepeat.getText())) {
            utils.openDialog("Hasło", "Podane hasła nie są takie same");
            result = false;
        }
        return result;
    }
}

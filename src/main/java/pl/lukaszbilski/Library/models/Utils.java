package pl.lukaszbilski.Library.models;

import javafx.scene.control.Alert;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Utils {


    public static void openDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static int ifExistInDataBase(String candidate){
        Statement statement = MariadbConnector.getInstance().getNewStatemnt();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) FROM user WHERE login = '"+ candidate + "'");
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static boolean isEmpty(String input){
        return (input.trim().length() < 3 || input.trim().length() > 25);
    }

    public static boolean regexEmail(String email) {
        return !Pattern.matches(".+@(.+)\\..+", email);
    }

    public static boolean regexPhone(String phoneNumber) {
        return !Pattern.matches("\\d{9}", phoneNumber);
    }

    public static String hashPassword(String password){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hasyArray = digest.digest(password.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hasyArray);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

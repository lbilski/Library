package pl.lukaszbilski.Library.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Utils {

    public void openDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int ifExistInDataBase(String candidate){
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

    public boolean isEmpty(String input){
        return (input.trim().length() < 3 || input.trim().length() > 25);
    }

    public boolean regexEmail(String email) {
        return !Pattern.matches(".+@(.+)\\..+", email);
    }

    public boolean regexPhone(String phoneNumber) {
        return !Pattern.matches("\\d{9}", phoneNumber);
    }

    public String hashPassword(String password){
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

    public void logout(MouseEvent event) throws IOException{
        Parent mainPage = FXMLLoader.load(getClass().getResource("/fxml/mainView.fxml"));
        Stage stageToClose = (Stage)((Node) event.getSource()).getScene().getWindow();
        stageToClose.close();
        Stage stage = new Stage();
        stage.setScene(new Scene(mainPage));
        stage.setTitle("Biblioteka");
        stage.show();
    }

    //get all boooks from database
    public ObservableList<Book> getBooks(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        Statement statement = MariadbConnector.getInstance().getNewStatemnt();

        try {
            ResultSet bookFromDB = statement.executeQuery("SELECT * FROM books");
            while (bookFromDB.next()){
                books.add(new Book(
                        bookFromDB.getInt("books_id"),
                        bookFromDB.getString("tytuł"),
                        bookFromDB.getString("autor"),
                        bookFromDB.getString("gatunek"),
                        bookFromDB.getInt("rokWydania"),
                        bookFromDB.getInt("liczbaStron"),
                        bookFromDB.getInt("liczbaSztuk")
                ));
            }
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

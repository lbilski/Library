package pl.lukaszbilski.Library.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.controllers.EditPasswordController;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Utils {

    Statement statement = MariadbConnector.getInstance().getNewStatement();

    //function show information dialog
    public void openDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //function return true if login is exist in database
    public boolean isLoginExist(String login){
        Statement statement = MariadbConnector.getInstance().getNewStatement();
        String query = "SELECT login FROM user WHERE login = '" + login + "' LIMIT 1";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean isBookRented(int book_id, int client_id){
        String query = "SELECT rented_id FROM rented WHERE id_ksiazki = '"+book_id+"' AND id_klienta = '"+client_id+"'";

        try {
            PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery(query);

            preparedStatement.close();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean isEmpty(String input){
        return (input.trim().length() < 3 || input.trim().length() > 25);
    }

    public boolean regexEmail(String email) {
        return !Pattern.matches(".+@(.+)\\..+", email);
    }

    //check phone number is a correct form
    public boolean regexPhone(String phoneNumber) {
        return !Pattern.matches("\\d{9}", phoneNumber);
    }

    //method hashs the password to save it to database in a safe form
    public String hashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hasyArray = digest.digest(password.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hasyArray);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //method logout user to MainView
    public void logout(MenuButton button) throws IOException{
        Parent mainPage = FXMLLoader.load(getClass().getResource("/fxml/mainView.fxml"));
        Stage stageToClose = (Stage) button.getScene().getWindow();
        stageToClose.close();
        Stage stage = new Stage();
        stage.setScene(new Scene(mainPage));
        stage.setTitle("Biblioteka");
        stage.show();
    }

    //get all books from database
    public ObservableList<Book> getBooks(){
        ObservableList<Book> books = FXCollections.observableArrayList();

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

    //get all rented books from database
    public ObservableList<RentedBook> getRentedBooks(int activeId){
        ObservableList<RentedBook> rentedBooks = FXCollections.observableArrayList();

        String query = "SELECT * FROM rented as r JOIN books as b " +
                "ON r.id_ksiazki = b.books_id " +
                "WHERE id_klienta = '" + activeId + "' ";

        try {
            ResultSet rentedBooksFromDatabse = statement.executeQuery(query);

            while(rentedBooksFromDatabse.next()){
                rentedBooks.add(new RentedBook(
                        rentedBooksFromDatabse.getInt("rented_id"),
                        rentedBooksFromDatabse.getInt("id_ksiazki"),
                        rentedBooksFromDatabse.getDate("data_wypozyczenia"),
                        rentedBooksFromDatabse.getDate("data_zwrotu"),
                        rentedBooksFromDatabse.getInt("ilosc"),
                        rentedBooksFromDatabse.getString("tytuł"),
                        rentedBooksFromDatabse.getString("autor"),
                        rentedBooksFromDatabse.getString("gatunek")
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentedBooks;
    }

    //open edit password controller
    public void editPassword(User activeUser){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editPassword.fxml"));
            Parent root = loader.load();
            EditPasswordController controller = loader.getController();
            controller.activeUser = activeUser;
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

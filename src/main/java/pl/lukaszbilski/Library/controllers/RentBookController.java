package pl.lukaszbilski.Library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.Book;
import pl.lukaszbilski.Library.models.MariadbConnector;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RentBookController implements Initializable {

    @FXML
    TextArea selectedBook;
    @FXML
    Button confirmButton;
    @FXML
    ChoiceBox<Integer> quantity;

    Book candidateBook = new Book();

    private ObservableList<Integer> quantityToRent = FXCollections.observableArrayList();
    private Statement statement = MariadbConnector.getInstance().getNewStatemnt();

    public void initialize(URL location, ResourceBundle resources) {
        quantityToRent.add(1);
        quantityToRent.add(2);
        quantity.setItems(quantityToRent);
    }

    public void confirmRent() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();

        try {
            String query = "UPDATE books SET liczbaSztuk = ? WHERE books_id = ?";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, candidateBook.getQuantity() - quantity.getValue());
            preparedStatement.setInt(2, candidateBook.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    public void initSelectedBook(){
        selectedBook.setText("\"" + candidateBook.getTitle() + "\", " + candidateBook.getAuthor() + ", " + candidateBook.getGendre() + ", Dostępna ilość: " + candidateBook.getQuantity());
    }

}

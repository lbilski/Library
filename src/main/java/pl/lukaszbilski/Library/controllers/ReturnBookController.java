package pl.lukaszbilski.Library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.Book;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.RentedBook;
import pl.lukaszbilski.Library.models.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnBookController{

    @FXML
    ChoiceBox<Integer> quantity;

    private Utils utils;
    private Book book = new Book();
    private ObservableList<Integer> quantityToReturn = FXCollections.observableArrayList();
    RentedBook rentedBook;

    public void confirmReturn (){
        Stage stage = (Stage) quantity.getScene().getWindow();
        String query;
        int quantityValue = quantity.getValue();

        setBook();

        if(quantityValue < rentedBook.ilosc){
            query = "UPDATE rented SET ilosc = ? WHERE rented_id = ?";
            try {
                PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
                preparedStatement.setInt(1, quantityValue);
                preparedStatement.setInt(2, rentedBook.rented_id);

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                utils.openDialog("Biblioteka", "Nie udało się zwrócić książki");
                e.printStackTrace();
                return;
            }
        }else{
            query = "DELETE FROM rented WHERE rented_id = ?";
            try {
                PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
                preparedStatement.setInt(1, rentedBook.rented_id);

                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                utils.openDialog("Biblioteka", "Nie udało się zwrócić książki");
                e.printStackTrace();
                return;
            }
        }

        updateBooks();

        stage.close();
    }

    //setting quantity possible to return
    public void setQuantity(){
        for(int i = 1; i <= rentedBook.ilosc; i++){
            quantityToReturn.add(i);
        }
        quantity.setItems(quantityToReturn);
    }

    //setting book object
    private void setBook(){
        String query = "SELECT books_id, liczbaSztuk FROM books WHERE books_id =?";
        try {
            PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setInt(1, rentedBook.id_ksiazki);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            book.setId(resultSet.getInt("books_id"));
            book.setQuantity(resultSet.getInt("liczbaSztuk"));

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update book's quantity in database
    private void updateBooks(){
        String query = "UPDATE books SET liczbaSztuk = ? WHERE books_id =?";
        try {
            PreparedStatement preparedStatement = MariadbConnector.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setInt(1, book.getQuantity() + quantity.getValue());
            preparedStatement.setInt(2, book.getId());

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package pl.lukaszbilski.Library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.lukaszbilski.Library.models.Book;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RentBookController implements Initializable {

    @FXML
    TextArea selectedBook;
    @FXML
    Button confirmButton;
    @FXML
    ChoiceBox<Integer> quantity;
    @FXML
    DatePicker rentalDate, returnDate;

    Book candidateBook = new Book();
    User activeUser;
    private Utils utils = new Utils();
    private ObservableList<Integer> quantityToRent = FXCollections.observableArrayList();
    private Statement statement = MariadbConnector.getInstance().getNewStatemnt();

    public void initialize(URL location, ResourceBundle resources) {
        quantityToRent.add(1);
        quantityToRent.add(2);
        quantity.setItems(quantityToRent);
    }

    public void checkRentalDate(){
        DatePicker actualDate = new DatePicker(LocalDate.now());
        final Callback<DatePicker, DateCell> rentalDateCell;

        rentalDateCell = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(actualDate.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }else if(item.isAfter(actualDate.getValue().plusMonths(3))){
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
        rentalDate.setDayCellFactory(rentalDateCell);
        returnDate.setValue(null);
    }

    public void checkReturnDate() {
        if (rentalDate.getValue() == null) {
            utils.openDialog("Biblioteka", "Wybierz datę wypożyczenia");
        } else {
            final Callback<DatePicker, DateCell> returnDateCell;

            returnDateCell = (final DatePicker datePicker) -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item.isBefore(rentalDate.getValue().plusDays(1))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }else if(item.isAfter(rentalDate.getValue().plusMonths(3))){
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            };
            returnDate.setDayCellFactory(returnDateCell);
        }
    }

    public void confirmRent() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        if(rentalDate.getValue() == null || returnDate.getValue() == null || quantity.getValue() == null){
            utils.openDialog("Biblioteka", "Uzupełnij wymagane pola");
            return;
        }


        try {
            String queryInsert = "INSERT INTO rented(id_ksiazki, id_klienta, data_wypozyczenia, data_zwrotu, ilosc, potwierdzenie) VALUES(?,?,?,?,?,?)";
            PreparedStatement insertStatemnt = statement.getConnection().prepareStatement(queryInsert);
            insertStatemnt.setInt(1, candidateBook.getId());
            insertStatemnt.setInt(2, activeUser.getUser_id());
            insertStatemnt.setDate(3, Date.valueOf(rentalDate.getValue()));
            insertStatemnt.setDate(4, Date.valueOf(returnDate.getValue()));
            insertStatemnt.setInt(5, quantity.getValue());
            insertStatemnt.setBoolean(6, false);

            insertStatemnt.execute();
            insertStatemnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String query = "UPDATE books SET liczbaSztuk = ? WHERE books_id = ?";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, candidateBook.getQuantity() - quantity.getValue());
            preparedStatement.setInt(2, candidateBook.getId());

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    public void initSelectedBook(){
        selectedBook.setText("\"" + candidateBook.getTitle() + "\", " + candidateBook.getAuthor() + ", " + candidateBook.getGendre() + ", Dostępna ilość: " + candidateBook.getQuantity());
    }

}

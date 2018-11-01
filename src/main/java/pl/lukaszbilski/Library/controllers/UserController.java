package pl.lukaszbilski.Library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pl.lukaszbilski.Library.models.Book;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.Utils;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserController implements Initializable{


    @FXML
    TextArea descriptionText;

    @FXML
    TableView<Book> tableBooks;
    @FXML
    TableColumn<Book, Integer> col_ID;
    @FXML
    TableColumn<Book, String> col_title;
    @FXML
    TableColumn<Book, String> col_author;
    @FXML
    TableColumn<Book, String> col_gendre;
    @FXML
    TableColumn<Book, Integer> col_publishment;
    @FXML
    TableColumn<Book, Integer> col_sheets;
    @FXML
    TableColumn<Book, Integer> col_quantity;


    private Utils utils = new Utils();
    private Statement statement = MariadbConnector.getInstance().getNewStatemnt();


    public void initialize(URL location, final ResourceBundle resources) {

        col_ID.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        col_title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        col_gendre.setCellValueFactory(new PropertyValueFactory<Book, String>("gendre"));
        col_publishment.setCellValueFactory(new PropertyValueFactory<Book, Integer>("publishment"));
        col_sheets.setCellValueFactory(new PropertyValueFactory<Book, Integer>("sheets"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));

        tableBooks.setItems(utils.getBooks());
        tableBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    ResultSet description = statement.executeQuery("SELECT opis FROM books WHERE id= '" + tableBooks.getSelectionModel().getSelectedItem().getId() + "'");
                    if(description.next()){
                        descriptionText.setText(description.getString("opis"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void logout(MouseEvent event) throws IOException {
        utils.logout(event);
    }
}

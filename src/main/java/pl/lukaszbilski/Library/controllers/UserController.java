package pl.lukaszbilski.Library.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.lukaszbilski.Library.models.Book;
import pl.lukaszbilski.Library.models.MariadbConnector;
import pl.lukaszbilski.Library.models.User;
import pl.lukaszbilski.Library.models.Utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserController implements Initializable{

    @FXML
    TextArea descriptionText;

    @FXML
    Button rentBook;

    @FXML
    TableView<Book> tableBooks;
    @FXML
    TableColumn<Book, Integer> col_ID, col_publishment, col_sheets, col_quantity;
    @FXML
    TableColumn<Book, String> col_title, col_author, col_gendre;


    private Utils utils = new Utils();
    private Book candidateBook = new Book();
    User activeUser = new User();
    private Statement statement = MariadbConnector.getInstance().getNewStatement();



    public void initialize(URL location, final ResourceBundle resources) {

        col_title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        col_author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        col_gendre.setCellValueFactory(new PropertyValueFactory<Book, String>("gendre"));
        col_publishment.setCellValueFactory(new PropertyValueFactory<Book, Integer>("publishment"));
        col_sheets.setCellValueFactory(new PropertyValueFactory<Book, Integer>("sheets"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));

        tableBooks.setItems(utils.getBooks());

        tableBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                candidateBook = tableBooks.getSelectionModel().getSelectedItem();

                try {
                    ResultSet description = statement.executeQuery("SELECT opis FROM books WHERE books_id= '" + candidateBook.getId() + "'");
                    if(description.next()){
                        descriptionText.setText(description.getString("opis"));
                        rentBook.setDisable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void rentBook(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rentBook.fxml"));
            Parent root = loader.load();
            RentBookController rentBook = loader.getController();
            rentBook.candidateBook = candidateBook;
            rentBook.activeUser = activeUser;
            rentBook.initSelectedBook();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 480, 320));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            initialize(new URL("file:/" + "../fxml/userView.fxml"), null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        rentBook.setDisable(true);
    }

    public void logout(MouseEvent event) throws IOException {
        utils.logout(event);
    }
}

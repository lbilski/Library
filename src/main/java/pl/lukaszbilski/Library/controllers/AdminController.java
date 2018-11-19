package pl.lukaszbilski.Library.controllers;


import javafx.application.Preloader;
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
import pl.lukaszbilski.Library.models.*;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AdminController implements Initializable{

    @FXML
    Button logoutButton, rentBook, wypReturnBook, wypExtortRental;

    @FXML
    TextArea descriptionText, wypDescriptionText;

    @FXML
    Tab tabRentedBooks;

    @FXML
    TableView<Book> tableBooks;
    @FXML
    TableColumn<Book, Integer> col_ID, col_publishment, col_sheets, col_quantity;
    @FXML
    TableColumn<Book, String> col_title, col_author, col_gendre;

    @FXML
    TableView<RentedBook> tableRentedBooks;
    @FXML
    TableColumn<RentedBook, Integer> col_RentIlosc;
    @FXML
    TableColumn<RentedBook, String> col_RentTytuł, col_RentAutor, col_RentGatunek;
    @FXML
    TableColumn<RentedBook, Date> col_RentDataWypozyczenia, col_RentDataZwrotu;

    private Book candidateBook = new Book();
    private RentedBook candidateRentedBook = new RentedBook();
    private Utils utils = new Utils();
    private Statement statement = MariadbConnector.getInstance().getNewStatemnt();
    public User activeAdmin = new User();

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
                    ResultSet description = statement.executeQuery("SELECT opis FROM books WHERE books_id= '" + tableBooks.getSelectionModel().getSelectedItem().getId() + "'");
                    if(description.next()){
                        descriptionText.setText(description.getString("opis"));
                        rentBook.setDisable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        tableRentedBooks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                candidateRentedBook = tableRentedBooks.getSelectionModel().getSelectedItem();

                try {
                    ResultSet description = statement.executeQuery("SELECT opis FROM books WHERE books_id= '" + tableRentedBooks.getSelectionModel().getSelectedItem().getId_ksiazki() + "'");
                    if(description.next()){
                        wypDescriptionText.setText(description.getString("opis"));
                        wypReturnBook.setDisable(false);
                        wypExtortRental.setDisable(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        tabRentedBooks.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                col_RentTytuł.setCellValueFactory(new PropertyValueFactory<RentedBook, String>("tytuł"));
                col_RentAutor.setCellValueFactory(new PropertyValueFactory<RentedBook, String>("autor"));
                col_RentGatunek.setCellValueFactory(new PropertyValueFactory<RentedBook, String>("gatunek"));
                col_RentDataWypozyczenia.setCellValueFactory(new PropertyValueFactory<RentedBook, Date>("data_wypozyczenia"));
                col_RentDataZwrotu.setCellValueFactory(new PropertyValueFactory<RentedBook, Date>("data_zwrotu"));
                col_RentIlosc.setCellValueFactory(new PropertyValueFactory<RentedBook, Integer>("ilosc"));

                tableRentedBooks.setItems(utils.getRentedBooks(activeAdmin.getUser_id()));

                wypExtortRental.setDisable(true);
                wypReturnBook.setDisable(true);
               }
        });
    }

    public void rentBook(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rentBook.fxml"));
            Parent root = loader.load();
            RentBookController rentBook = loader.getController();
            rentBook.candidateBook = candidateBook;
            rentBook.activeUser = activeAdmin;
            rentBook.initSelectedBook();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 480, 320));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            initialize(new URL("file:/" + "../fxml/adminView.fxml"), null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        rentBook.setDisable(true);
    }

    public void logout(MouseEvent event) throws IOException {
        utils.logout(event);
    }
}

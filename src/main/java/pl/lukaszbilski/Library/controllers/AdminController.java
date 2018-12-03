package pl.lukaszbilski.Library.controllers;

import javafx.event.ActionEvent;
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
import javafx.util.Callback;
import pl.lukaszbilski.Library.models.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminController implements Initializable{

    @FXML
    Button rentBook, wypReturnBook, wypExtortRental;

    @FXML
    MenuButton myAccount;

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
    TableColumn<RentedBook, Integer> col_RentQuantity;
    @FXML
    TableColumn<RentedBook, String> col_RentTitle, col_RentAuthor, col_RentGendre;
    @FXML
    TableColumn<RentedBook, Date> col_RentRentedDate, col_RentReturnDate;

    private Book candidateBook = new Book();
    private RentedBook candidateRentedBook = new RentedBook();
    private Utils utils = new Utils();
    private Statement statement = MariadbConnector.getInstance().getNewStatement();
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
                    setListOfRentedBooks();
               }
        });
    }

    public void rentBook(){
        if(utils.isBookRented(candidateBook.getId(), activeAdmin.getUser_id())){
            utils.openDialog("Biblioteka", "Masz już wypożyczoną tą pozycję");
            rentBook.setDisable(true);
            return;
        }

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

    public void returnBook(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/returnBook.fxml"));
            Parent root = loader.load();
            ReturnBookController controller = loader.getController();
            controller.rentedBook = candidateRentedBook;
            controller.setQuantity();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListOfRentedBooks();
        try {
            initialize(new URL("file:/" + "../fxml/adminView.fxml"), null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        wypReturnBook.setDisable(true);
    }

    public void editPassword(){
        utils.editPassword(activeAdmin);
    }

    public void logout() throws IOException {
        utils.logout(myAccount);
    }

    public void setListOfRentedBooks(){
        LocalDate date = LocalDate.now();

        col_RentTitle.setCellValueFactory(new PropertyValueFactory<>("tytuł"));
        col_RentAuthor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        col_RentGendre.setCellValueFactory(new PropertyValueFactory<>("gatunek"));
        col_RentRentedDate.setCellValueFactory(new PropertyValueFactory<>("data_wypozyczenia"));
        col_RentReturnDate.setCellValueFactory(new PropertyValueFactory<>("data_zwrotu"));
        col_RentReturnDate.setCellFactory(new Callback<TableColumn<RentedBook, Date>, TableCell<RentedBook, Date>>() {
            @Override
            public TableCell<RentedBook, Date> call(TableColumn<RentedBook, Date> param) {
                return new TableCell<RentedBook, Date>(){
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        if(item != null){
                            setText(item.toString());
                            if(item.before(Date.valueOf(date))){
                                setStyle("-fx-background-color: #FF0000; -fx-opacity: 80%");
                            } else if(item.before(Date.valueOf(date.plusDays(4)))){
                                setStyle("-fx-background-color: #ff9900; -fx-opacity: 80%");
                            }else {
                                setStyle("-fx-background-color: #33ff33; -fx-opacity: 80%");
                            }
                        }
                    }
                };
            }
        });
        col_RentQuantity.setCellValueFactory(new PropertyValueFactory<RentedBook, Integer>("ilosc"));

        tableRentedBooks.setItems(utils.getRentedBooks(activeAdmin.getUser_id()));

        wypExtortRental.setDisable(true);
        wypReturnBook.setDisable(true);
    }
}

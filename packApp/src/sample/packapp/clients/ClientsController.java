package sample.packapp.clients;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class ClientsController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private TextField clientNumTextField;
    @FXML
    private TextField clientNameTextField;

    @FXML
    private TableView<Clients> clientsTableView;
    @FXML
    private TableColumn<Clients, Integer> numClientColumn;
    @FXML
    private TableColumn<Clients, String> ClientNameColumn;
    @FXML
    private TableColumn<Clients, String> lastCmdColumn;
    @FXML
    private TableColumn<Clients, String> NbCmdColumn;
    @FXML
    private Button modifier;
    @FXML
    private Button commandes;
    @FXML
    private Button chercher;


    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showClients();
        //modifier.setDisable(true);
        //chercher.setDisable(true);
    }

    public Connection getConnection() {

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            return connection;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    public ObservableList<Clients> getClientsList() {

        ObservableList<Clients> clientsList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM clients";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Clients clients;
            while (resultSet.next()) {
                clients = new Clients(resultSet.getInt("numClient"), resultSet.getString("ClientName"), resultSet.getString("lastCmd"), resultSet.getInt("NbCmd"));
                clientsList.add(clients);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientsList;
    }

    public void showClients() {

        ObservableList<Clients> list = getClientsList();

        numClientColumn.setCellValueFactory(new PropertyValueFactory<Clients, Integer>("numClients"));
        ClientNameColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("ClientsName"));
        lastCmdColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("lastCmd"));
        NbCmdColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("NbCmd"));

        clientsTableView.setItems(list);
    }
    private void executeQuery(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearFields() {
        clientNumTextField.clear();
        clientNameTextField.clear();
    }


}

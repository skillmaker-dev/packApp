package sample.packapp.clients.AffichageDesCommandes;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.util.Duration;
import sample.packapp.commandes.Orders;
import sample.packapp.depot.Products;

import java.io.IOException;
import java.sql.*;

public class AffichageDesCommandesController  {
    @FXML
    private AnchorPane container;

   private Parent root;

    @FXML
    private Button RetourButton;
    @FXML
    private Button NouvelleCommandeButton;
    @FXML
    private Button modifierButton;
    @FXML
    private TableView<Orders> CommandesTableView;
    @FXML
    private TableColumn<Orders, String> RefCommandeColumn;
    @FXML
    private TableColumn<Orders, String> ProductNameColumn;
    @FXML
    private TableColumn<Orders, Double> PriceColumn;
    @FXML
    private TableColumn<Orders, String> StatutColumn;
    @FXML
    private TableColumn<Orders, Integer> QuantiteColumn;
    @FXML
    private Label clientLabel;


    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../_Clients/clients.fxml"));
        root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.close();
    }

    public void HandleNouvelleCommandeButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NouvelleCommande2.fxml"));
        root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    public void setSelectedClient(String selectedClient) {

        ObservableList<Orders> clientOrdersList = FXCollections.observableArrayList();
        ObservableList<Orders> list = clientOrdersList;
        clientLabel.setText(selectedClient);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM orders WHERE fullName = '" + clientLabel.getText() +"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Orders orders;
            while (resultSet.next()) {
                orders = new Orders(resultSet.getString("reference"),resultSet.getString("fullName")
                        ,resultSet.getString("phone"),resultSet.getString("email"),
                        resultSet.getString("address"),resultSet.getString("gender"),
                        resultSet.getString("product"),resultSet.getDouble("price"),
                        resultSet.getInt("amount"),resultSet.getString("status"));
                clientOrdersList.add(orders);
            }
            RefCommandeColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("reference"));
            ProductNameColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("product"));
            PriceColumn.setCellValueFactory(new PropertyValueFactory<Orders,Double>("price"));
            QuantiteColumn.setCellValueFactory(new PropertyValueFactory<Orders,Integer>("amount"));
            StatutColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));
            CommandesTableView.setItems(clientOrdersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
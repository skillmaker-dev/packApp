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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.util.Duration;
import sample.packapp.clients._Clients.Clients;
import sample.packapp.commandes.Orders;
import sample.packapp.depot.Products;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AffichageDesCommandesController  implements Initializable {
    @FXML
    private AnchorPane container;

   private Parent root;
    @FXML
    private Button orderItems;
    @FXML
    private Button RetourButton;
    @FXML
    private Button modifierButton;
    @FXML
    private TableView<Orders> CommandesTableView;
    @FXML
    private TableColumn<Orders, Integer> RefCommandeColumn;
    @FXML
    private TableColumn<Orders, Double> PriceColumn;
    @FXML
    private TableColumn<Orders, String> StatutColumn;
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

    public void setSelectedClient(int ClientId,String clientName) {

        ObservableList<Orders> clientOrdersList = FXCollections.observableArrayList();
        ObservableList<Orders> list = clientOrdersList;

        clientLabel.setText(clientName);

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM orders WHERE client_id = " + ClientId +"";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Orders orders;
            while (resultSet.next()) {
                orders = new Orders(resultSet.getInt("order_id"),resultSet.getInt("client_id")
                        ,clientName,resultSet.getDouble("totalPrice"),resultSet.getString("status"));
                clientOrdersList.add(orders);
            }
            RefCommandeColumn.setCellValueFactory(new PropertyValueFactory<Orders,Integer>("orderId"));
            PriceColumn.setCellValueFactory(new PropertyValueFactory<Orders,Double>("totalPrice"));
            StatutColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));
            CommandesTableView.setItems(clientOrdersList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void orderItemsButton(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderItems.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            OrderItemsController orderItemsController = fxmlLoader.getController();
            orderItemsController.getOrderId(selectedOrderID);
            Stage stage = new Stage();
            stage.setTitle("Order Items");

            Image myIcon = new Image("sample/icon/PackageAPP.png");
            stage.getIcons().add(myIcon);
            stage.setScene(new Scene(root1));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public int selectedOrderID;
    @FXML
    public void handleMouseAction(MouseEvent event) throws SQLException {

        if(!CommandesTableView.getSelectionModel().isEmpty()){
            Orders orders = CommandesTableView.getSelectionModel().getSelectedItem();
            selectedOrderID = orders.getOrderId();
            orderItems.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderItems.setDisable(true);
    }
}
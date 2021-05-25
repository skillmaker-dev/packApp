package sample.packapp.clients.AffichageDesCommandes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.packapp.commandes.Orders;

import java.io.IOException;
import java.sql.*;

public class OrderItemsController  {

    private Parent root;
    private int orderId=0;
    @FXML
    private TableView<OrderItems> ordersTableView;
    @FXML
    private TableColumn<OrderItems, Integer> idColumn;
    @FXML
    private TableColumn<OrderItems, Double> priceColumn;
    @FXML
    private TableColumn<OrderItems, String> productColumn;
    @FXML
    private TableColumn<OrderItems, Integer> quantityColumn;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../_Clients/clients.fxml"));
        root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.close();
    }

    public void getOrderId(int id)
    {
        ObservableList<OrderItems> orderItemsList = FXCollections.observableArrayList();
        String productName = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM order_items WHERE order_id = " + id +"";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2;
            OrderItems orderItems;
            while (resultSet.next()) {
                String query1 =  "SELECT * FROM products WHERE product_id = " + resultSet.getInt("product_id") + "";
                 resultSet2 = statement2.executeQuery(query1);
                 while(resultSet2.next())
                 {
                     productName = resultSet2.getString("name");
                 }
                orderItems = new OrderItems(resultSet.getInt("product_id"),productName
                        ,resultSet.getDouble("price"),resultSet.getInt("quantity"));
                orderItemsList.add(orderItems);
            }
            idColumn.setCellValueFactory(new PropertyValueFactory<OrderItems,Integer>("productId"));
            productColumn.setCellValueFactory(new PropertyValueFactory<OrderItems,String>("productName"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderItems,Integer>("quantity"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<OrderItems,Double>("price"));
            ordersTableView.setItems(orderItemsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}

package sample.packapp.commandes;

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
import sample.packapp.depot.Products;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class CommandesController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private TextField clientField;
    @FXML
    private TextField telField;
    @FXML
    private TextField adrField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField refField;
    @FXML
    private TextField prodField;
    @FXML
    private ChoiceBox<String> statusChoice;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Orders> ordersTableView;
    @FXML
    private TableColumn<Orders,String> refColumn;
    @FXML
    private TableColumn<Orders,String> clientColumn;
    @FXML
    private TableColumn<Orders,Double> priceColumn;
    @FXML
    private TableColumn<Orders,String> statusColumn;

    private String[] status = {"En cours" , "Livrée" , "Annulée"};

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
        statusChoice.getItems().setAll(status);
        statusChoice.setValue("Statut : ");
        showOrders();
        editButton.setDisable(true);
        deleteButton.setDisable(true);
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

    public ObservableList<Orders> getOrdersList() {

        ObservableList<Orders> ordersList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM orders";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Orders orders;
            while (resultSet.next()) {
                orders = new Orders(resultSet.getString("reference"),resultSet.getString("fullName")
                        ,resultSet.getString("phone"),resultSet.getString("email"),
                        resultSet.getString("address"),resultSet.getString("gender"),
                        resultSet.getString("product"),resultSet.getDouble("price"),
                        resultSet.getInt("amount"),resultSet.getString("status"));
                ordersList.add(orders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public void showOrders() {

        ObservableList<Orders> list = getOrdersList();

        refColumn.setCellValueFactory(new PropertyValueFactory<Orders,String >("reference"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("fullName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Orders,Double>("Price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));

        ordersTableView.setItems(list);
    }

    public void handleEditButton(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modifierCommande.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Modifier Commande");
            stage.setResizable(false);
            Image myIcon = new Image("sample/icon/PackageAPP.png");
            stage.getIcons().add(myIcon);
            stage.setScene(new Scene(root1));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You can not Modify an order!!");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        }

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

    private void clearFields() {

        clientField.clear();
        telField.clear();
        emailField.clear();
        refField.clear();
        adrField.clear();
        prodField.clear();
        statusChoice.setValue("Statut : ");

    }

    @FXML
    private void handleMouseAction(MouseEvent event) {

        if(!ordersTableView.getSelectionModel().isEmpty()){
            Orders orders = ordersTableView.getSelectionModel().getSelectedItem();
            refField.setText("" + orders.getReference());
            clientField.setText("" + orders.getFullName());
            telField.setText("" + orders.getPhone());
            adrField.setText("" + orders.getAddress());
            emailField.setText("" + orders.getEmail());
            prodField.setText("" + orders.getProduct());
            statusChoice.setValue("" + orders.getStatus());
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public void handleSearchButton(ActionEvent event) {



    }

    public void handleDeleteButton(ActionEvent event) {

        if(ordersTableView.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must select an order to DELETE it !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        }else{
            String query = "DELETE FROM orders WHERE fullName = '" + clientField.getText() + "'";
            executeQuery(query);
            showOrders();
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

    }
}
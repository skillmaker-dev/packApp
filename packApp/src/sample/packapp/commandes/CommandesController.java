package sample.packapp.commandes;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CommandesController implements Initializable {

    @FXML
    private AnchorPane container;
    @FXML
    private Button retour;
    @FXML
    private TextField refField;
    @FXML
    private Button searchButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Orders> ordersTableView;
    @FXML
    private TableColumn<Orders,Integer> refColumn;
    @FXML
    private TableColumn<Orders,String> clientColumn;
    @FXML
    private TableColumn<Orders,Integer> idColumn;
    @FXML
    private TableColumn<Orders,Double> priceColumn;
    @FXML
    private TableColumn<Orders,String> statusColumn;


    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        Parent root = loader.load();
        Scene scene = retour.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        Connection connection1 = getConnection();
        try {
            String query = "SELECT * FROM orders";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Statement statement1 = connection.createStatement();;
            ResultSet resultSet1;
            String name = null;
            Orders orders;
            while (resultSet.next()) {
                String query1 = "SELECT * FROM clients WHERE client_id = " + resultSet.getInt("client_id") + "";
                resultSet1 = statement1.executeQuery(query1);
                while (resultSet1.next()) {
                   name = resultSet1.getString("fullname");
                }
                orders = new Orders(resultSet.getInt("order_id"),resultSet.getInt("client_id")
                        ,name,resultSet.getDouble("totalPrice"),resultSet.getString("status"));
                ordersList.add(orders);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public void showOrders() {

        ObservableList<Orders> list = getOrdersList();

        refColumn.setCellValueFactory(new PropertyValueFactory<Orders,Integer> ("orderId"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Orders,Integer> ("clientId"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("fullName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Orders,Double>("totalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Orders,String>("status"));

        ordersTableView.setItems(list);
    }

    public void handleEditButton(ActionEvent event) {

        try {
            String selectedRef = refField.getText();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modifierCommande.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ModifierCommandeController modifierCommandeController = fxmlLoader.getController();
            modifierCommandeController.setSelectedReference(selectedRef);
            Stage stage = new Stage();
            stage.setTitle("Modifier Commande");
            stage.setResizable(false);
            Image myIcon = new Image("sample/icon/PackageAPP.png");
            stage.getIcons().add(myIcon);
            stage.setScene(new Scene(root1));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void executeQuery(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        refField.clear();
    }

    @FXML
    private void handleMouseAction(MouseEvent event) {

        if(!ordersTableView.getSelectionModel().isEmpty()){
            Orders orders = ordersTableView.getSelectionModel().getSelectedItem();
            refField.setText("" + orders.getOrderId());
            editButton.setDisable(false);
            deleteButton.setDisable(false);
        }
    }

    public void handleSearchButton(ActionEvent event) throws SQLException {

        int index = -1, totalRows = 0;
        if(refField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Please enter a reference !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            refField.clear();
        } else {
            String query = "SELECT * FROM orders";
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                index++;
                if (resultSet.getString("order_id").equals(refField.getText())) {
                    break;
                }
            }
            Connection connection2 = getConnection();
            Statement statement2 = connection2.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(query);
            while (resultSet2.next()) {
                totalRows++;
            }
            index++;
            int ref = (int) ordersTableView.getColumns().get(0).getCellObservableValue(index - 1).getValue();
            if (index == totalRows && Integer.parseInt(refField.getText()) == ref) {
                ordersTableView.getSelectionModel().select(index - 1);
            } else if (index < totalRows) {
                ordersTableView.getSelectionModel().select(index - 1);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("No result match with this reference !");
                alert.setContentText("Click Ok to Continue");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                refField.clear();
            }
            connection.close();
            connection2.close();
        }

    }

    public void handleDeleteButton(ActionEvent event) throws SQLException {

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
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "DELETE FROM orders WHERE order_id = " + refField.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            String query2 = "DELETE FROM order_items WHERE order_id = " + refField.getText() + "";
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate(query2);
            connection.close();
            showOrders();
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

    }

    public void refresh(ActionEvent event) {
        showOrders();
        refField.clear();
        deleteButton.setDisable(true);
        editButton.setDisable(true);
    }
}
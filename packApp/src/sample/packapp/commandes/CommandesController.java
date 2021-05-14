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
import javafx.scene.Node;
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
import sample.packapp.depot.Products;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
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
    private TableColumn<Orders,String> refColumn;
    @FXML
    private TableColumn<Orders,String> clientColumn;
    @FXML
    private TableColumn<Orders,Double> priceColumn;
    @FXML
    private TableColumn<Orders,String> statusColumn;

    private String[] status = {"En cours" , "Livrée" , "Annulée"};
    private String selectedReference = "";

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        Parent root = loader.load();
        Scene scene = retour.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
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
            refField.setText("" + orders.getReference());
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
                if (resultSet.getString("reference").equals(refField.getText())) {
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
            String ref = (String) ordersTableView.getColumns().get(0).getCellObservableValue(index - 1).getValue();
            if (index == 5 && refField.getText().equals(ref)) {
                ordersTableView.getSelectionModel().select(index - 1);
            } else if (index < totalRows) {
                System.out.println(index);
                System.out.println(totalRows);
                ordersTableView.getSelectionModel().select(index - 1);
            } else {
                System.out.println(index);
                System.out.println(totalRows);
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
            Connection connection2 = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            int nbrOfOrders = 0;
            String ref = "";
            String name = "";
            String query4 = "SELECT * FROM orders WHERE reference = '" + refField.getText() + "'";
            Statement statement3 = connection2.createStatement();
            ResultSet resultSet2 = statement3.executeQuery(query4);
            while (resultSet2.next()) {
                ref = resultSet2.getString("reference");
            }
            String query = "DELETE FROM orders WHERE reference = '" + ref + "'";
            executeQuery(query);
            Connection connection1 = getConnection();
            String query5 = "SELECT fullName FROM orders WHERE reference = '" + refField.getText() + "'";
            Statement statement4 = connection1.createStatement();
            ResultSet resultSet3 = statement4.executeQuery(query5);
            while (resultSet3.next()) {
                name = resultSet3.getString("fullName");
            }
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query2 = "SELECT nbrOrders FROM clients WHERE name = '" + name + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query2);
            while (resultSet.next()) {
                nbrOfOrders = resultSet.getInt("nbrOrders") - 1;
            }
            String query3 = "UPDATE clients SET nbrOrders = " + nbrOfOrders + " WHERE name = '"
                    + name + "'";
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate(query3);
            showOrders();
            clearFields();
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }

    }
}
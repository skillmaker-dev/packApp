package sample.packapp.clients._Clients;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class ClientsController implements Initializable {

    @FXML
    private AnchorPane container;
    @FXML
    private Button retour;
    @FXML
    private TextField clientIdTextField;
    @FXML
    private TextField clientNameTextField;
    @FXML
    private TextField clientEmailTextField;
    @FXML
    private TableView<Clients> clientsTableView;
    @FXML
    private TableColumn<Clients, Integer> idClientColumn;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../mainPage/mainPage.fxml"));
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
        showClients();
        modifier.setDisable(true);
        chercher.setDisable(true);
        commandes.setDisable(true);
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
                clients = new Clients(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("lastOrder"), resultSet.getInt("NbrOrders"));
                clientsList.add(clients);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientsList;
    }

    public void showClients() {

        ObservableList<Clients> list = getClientsList();

        idClientColumn.setCellValueFactory(new PropertyValueFactory<Clients, Integer>("clientId"));
        ClientNameColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("clientName"));
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
        clientIdTextField.clear();
        clientNameTextField.clear();
    }

    public void handleEditButton(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ModifierClient/ModifierClient.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Modifier Client");
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
    public void handleCommandeButton(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../AffichageDesCommandes/AffichageDesCommandes.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher les commandes");
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

    public void handleSearchButton(ActionEvent event) {

    }

    @FXML
    private void handleMouseAction(MouseEvent event) throws SQLException {

        if(!clientsTableView.getSelectionModel().isEmpty()){
            Connection connection = getConnection();
            String query = "SELECT * FROM orders WHERE fullName = '" + clientNameTextField.getText() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String email = resultSet.getString("email");
                clientEmailTextField.setText(email);
            }
            Clients clients = clientsTableView.getSelectionModel().getSelectedItem();
            clientIdTextField.setText("" + clients.getClientId());
            clientNameTextField.setText("" + clients.getClientName());
            modifier.setDisable(false);
            chercher.setDisable(false);
            commandes.setDisable(false);
        }
    }

}

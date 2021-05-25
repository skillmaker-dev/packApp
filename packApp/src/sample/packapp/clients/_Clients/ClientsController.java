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
import sample.packapp.clients.AffichageDesCommandes.AffichageDesCommandesController;
import sample.packapp.clients.ModifierClient.ModifierClientController;

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
    private TableView<Clients> clientsTableView;
    @FXML
    private TableColumn<Clients, Integer> idClientColumn;
    @FXML
    private TableColumn<Clients, String> ClientNameColumn;
    @FXML
    private TableColumn<Clients, String> phoneColumn;
    @FXML
    private TableColumn<Clients, String> emailColumn;
    @FXML
    private TableColumn<Clients, String> adrColumn;
    @FXML
    private Button modifier;
    @FXML
    private Button commandes;
    @FXML
    private Button chercher;
    @FXML
    private Button delete;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../mainPage/mainPage.fxml"));
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
        showClients();
        modifier.setDisable(true);
        commandes.setDisable(true);
        chercher.setDisable(false);
        delete.setDisable(true);
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
                clients = new Clients(resultSet.getInt("client_id"), resultSet.getString("fullname"), resultSet.getString("phone"), resultSet.getString("email"),resultSet.getString("address"));
                clientsList.add(clients);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientsList;
    }

    public void showClients() {

        ObservableList<Clients> list = getClientsList();

        idClientColumn.setCellValueFactory(new PropertyValueFactory<Clients, Integer>("clientId"));
        ClientNameColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("clientName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("email"));
        adrColumn.setCellValueFactory(new PropertyValueFactory<Clients, String>("address"));

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
            String selectedId = clientIdTextField.getText();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ModifierClient/ModifierClient.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ModifierClientController modifierClientController = fxmlLoader.getController();
            modifierClientController.setSelectedId(selectedId);
            Stage stage = new Stage();
            stage.setTitle("Modifier Client");

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
            int selectedClient = Integer.parseInt(clientIdTextField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../AffichageDesCommandes/AffichageDesCommandes.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            AffichageDesCommandesController affichageDesCommandesController = fxmlLoader.getController();
            affichageDesCommandesController.setSelectedClient(selectedClient,clientNameTextField.getText());
            Stage stage = new Stage();
            stage.setTitle("Afficher les commandes");

            Image myIcon = new Image("sample/icon/PackageAPP.png");
            stage.getIcons().add(myIcon);
            stage.setScene(new Scene(root1));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleDeleteButton() throws SQLException {
        if(clientNameTextField.getText().isEmpty() || clientIdTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must select a client to DELETE it !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        } else {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "DELETE FROM clients WHERE client_id = " + clientIdTextField.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
            showClients();
            clearFields();

        }
    }

    public void handleSearchButton(ActionEvent event) {

        try {
            int index = -1, totalRows = 0;
            if (clientIdTextField.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Please enter an id !");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                clientIdTextField.clear();
            } else {
                String query = "SELECT * FROM clients";
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    index++;
                    if (resultSet.getInt("client_id") == Integer.parseInt(clientIdTextField.getText())) {
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
                int id = (int) clientsTableView.getColumns().get(0).getCellObservableValue(index - 1).getValue();
                if (index == totalRows && Integer.parseInt(clientIdTextField.getText()) == id) {
                    clientsTableView.getSelectionModel().select(index - 1);
                } else if (index < totalRows) {
                    clientsTableView.getSelectionModel().select(index - 1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText("Client not found !");
                    alert.setContentText("Click Ok to Continue");
                    alert.show();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                    stage.getIcons().add(myIcone);
                    clientIdTextField.clear();
                }
                connection.close();
                connection2.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Please enter a valid value of ID!");
            alert.setContentText("Click Ok to Continue");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            clientIdTextField.clear();
        }

    }

    @FXML
    private void handleMouseAction(MouseEvent event) throws SQLException {

        if(!clientsTableView.getSelectionModel().isEmpty()){
            Clients clients = clientsTableView.getSelectionModel().getSelectedItem();
            clientIdTextField.setText("" + clients.getClientId());
            clientNameTextField.setText("" + clients.getClientName());
            modifier.setDisable(false);
            chercher.setDisable(true);
            commandes.setDisable(false);
            delete.setDisable(false);
        }
    }

    public void refresh(ActionEvent event) {
        showClients();
        modifier.setDisable(true);
        commandes.setDisable(true);
        chercher.setDisable(false);
        delete.setDisable(true);
        clearFields();
    }
}

package sample.packapp.clients;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import sample.packapp.commandes.Orders;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AffichageDesCommandesController  {
    private Stage stage;
    private Scene scene;
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
    private TableColumn<Orders, Integer> RefCommandeColumn;
    @FXML
    private TableColumn<Orders, String> ProductNameColumn;
    @FXML
    private TableColumn<Orders, Float> PriceColumn;
    @FXML
    private TableColumn<Orders, String> StatutColumn;
    @FXML
    private TableColumn<Orders, String> NameClientColumn;
    @FXML
    private TableColumn<Orders, String> QuantiteColumn;


    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clients.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void HandleNouvelleCommandeButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../nouvelleCommande/nouvelleCommande.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
package sample.packapp.clients.AffichageDesCommandes;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.util.Duration;
import sample.packapp.commandes.Orders;

import java.io.IOException;

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
}
package sample.packapp.clients;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;

public class AffichageDesCommandesController {
    @FXML
    private Button retourButton;
    @FXML
    private Button nouvelleCommandeButton;
    @FXML
    private Button modifierButton;
    @FXML
    private TableView<Commandes> commandesTableView;
    @FXML
    private TableColumn<Commandes, Integer> numCommandeColumn;
    @FXML
    private TableColumn<Commandes, String> CommandeNameColumn;
    @FXML
    private TableColumn<Commandes, Float> PriceColumn;
    @FXML
    private TableColumn<Commandes, String> StatutColumn;

}
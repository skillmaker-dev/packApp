package sample.packapp.commandes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class modifierCommandeController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Button enregistrerButton,annulerButton;
    @FXML
    private TextField nomField,telField,emailField,adrField,QuantiteField;
    @FXML
    private RadioButton femaleRadioButton,maleRadioButton;
    @FXML
    private ChoiceBox<String> commandeStatus;
    private String[] status = {"En cours" , "Livrée" , "Annulée"};


    public void annuler(ActionEvent e) throws IOException
    {
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }

    public void enregistrer(ActionEvent e) throws  IOException
    {
        //Write the save code here
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandeStatus.getItems().setAll(status);
    }
}

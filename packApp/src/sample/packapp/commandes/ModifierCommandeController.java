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
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifierCommandeController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField reference;
    @FXML
    private Button enregistrerButton;
    @FXML
    private Button annulerButton;
    @FXML
    private TextField nomField;
    @FXML
    private TextField telField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField adrField;
    @FXML
    private TextField QuantiteField;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private ChoiceBox<String> commandeStatus;
    private String[] status = {"En cours" , "Livrée" , "Annulée"};

    public void setSelectedReference(String selectedReference) {
        reference.setText(selectedReference);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM orders WHERE reference = '" + reference.getText() +"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                nomField.setText(resultSet.getString("fullName"));
                telField.setText(String.valueOf(resultSet.getInt("phone")));
                emailField.setText(resultSet.getString("email"));
                adrField.setText(resultSet.getString("address"));
                QuantiteField.setText(String.valueOf(resultSet.getInt("amount")));
                commandeStatus.setValue(resultSet.getString("status"));
                if(resultSet.getString("gender").equals("Homme")) {
                    maleRadioButton.setSelected(true);
                } else if(resultSet.getString("gender").equals("Femme")) {
                    femaleRadioButton.setSelected(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void annuler(ActionEvent e) throws IOException
    {
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }

    public void enregistrer(ActionEvent e)
    {
        String choice = "";
        if(maleRadioButton.isSelected()) {
            choice = "Homme";
        } else if(femaleRadioButton.isSelected()) {
            choice = "Femme";
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "UPDATE orders SET fullName = '" + nomField.getText() + "' , phone = " + telField.getText()
                    + " , email = '" + emailField.getText() + "' , address = '" + adrField.getText() + "' , amount = "
                    + QuantiteField.getText() + " , status = '" + commandeStatus.getValue() + "' , gender = '" +
                    choice + "' WHERE reference = '" + reference.getText() + "'";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            ((Node)(e.getSource())).getScene().getWindow().hide();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commandeStatus.getItems().setAll(status);
        reference.setEditable(false);

    }
}

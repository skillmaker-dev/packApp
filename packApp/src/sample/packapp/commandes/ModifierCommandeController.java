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
    private ChoiceBox<String> commandeStatus;
    private String[] status = {"In progress" , "Delivered" , "Canceled"};
    private  int clientId=0;
    public void setSelectedReference(String selectedReference) {
        reference.setText(selectedReference);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM orders WHERE order_id = '" + reference.getText() +"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next())
            {
                clientId = resultSet.getInt("client_id");
                commandeStatus.setValue(resultSet.getString("status"));
            }
            String query1= "SELECT * FROM clients WHERE client_id = '" + clientId +"'";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);
            while (resultSet1.next()) {
                nomField.setText(resultSet1.getString("fullname"));
                telField.setText(resultSet1.getString("phone"));
                emailField.setText(resultSet1.getString("email"));
                adrField.setText(resultSet1.getString("address"));

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void annuler(ActionEvent e)
    {
        ((Node)(e.getSource())).getScene().getWindow().hide();
    }

    public void enregistrer(ActionEvent e) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "UPDATE orders SET status = '" + commandeStatus.getValue() + "' WHERE order_id = " + reference.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            String query2 = "UPDATE clients SET fullname = '" + nomField.getText() + "' , phone = '" + telField.getText() + "', email = '" + emailField.getText() + "', address = '" + adrField.getText() + "' WHERE client_id = " + clientId + "";
            Statement statement1 = connection.createStatement();
            statement1.executeUpdate(query2);
            connection.close();
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

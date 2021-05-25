package sample.packapp.clients.ModifierClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ModifierClientController implements Initializable {

    @FXML
    private TextField fullNameField ;
    @FXML
    private TextField idField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private Button TerminerButton;
    @FXML
    private Button SaveButton;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../_Clients/clients.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void HandleEndButton(ActionEvent event) throws IOException{
        // get a handle to the stage
        Stage stage = (Stage) TerminerButton.getScene().getWindow();
        stage.close();
    }

    public void handleSaveButton(ActionEvent event) {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "UPDATE clients SET fullname = '" + fullNameField.getText() + "' , phone = '" + phoneField.getText() + "' , email = '" + emailField.getText()
                    + "' , address = '" + addressField.getText() + "' WHERE client_id =  " + idField.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            ((Node)(event.getSource())).getScene().getWindow().hide();
            connection.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void setSelectedId(String selectedId) {

        idField.setText(selectedId);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM clients WHERE client_id = '" + idField.getText() +"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                fullNameField.setText(resultSet.getString("fullname"));
                phoneField.setText(resultSet.getString("phone"));
                emailField.setText(resultSet.getString("email"));
                addressField.setText(resultSet.getString("address"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            idField.setDisable(true);
    }
}

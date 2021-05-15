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
    private TextField lastCmdField;
    @FXML
    private TextField nbrCmdField;
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
            String query = "UPDATE clients SET id = " + idField.getText() + " , name = '" + fullNameField.getText()
                    + "'";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void setSelectedId(String selectedId) {

        idField.setText(selectedId);
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM clients WHERE id = '" + idField.getText() +"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                fullNameField.setText(resultSet.getString("name"));
                nbrCmdField.setText(String.valueOf(resultSet.getInt("nbrOrders")));
                lastCmdField.setText(resultSet.getString("lastOrder"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lastCmdField.setEditable(false);
        nbrCmdField.setEditable(false);
    }
}

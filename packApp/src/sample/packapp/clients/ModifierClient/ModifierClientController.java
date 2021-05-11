package sample.packapp.clients.ModifierClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ModifierClientController {
    private Stage stage;
    private Scene scene;
    private Parent root;

@FXML
private   TextField    fullNameField ;
@FXML
private   TextField    phoneField ;

@FXML
private   TextField    emailField ;
@FXML
private   Spinner    NombreDeCommandeSpinner ;
/*@FXML
private DatePicker DateDeRejointDatePicker ;
@FXML
private DatePicker DateDeCommandeDatePicker ;*/
@FXML
private RadioButton maleRadio;
@FXML
private RadioButton femaleRadio;
@FXML
private Button TerminerButton;
@FXML
private Button SaveButton;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../_Clients/clients.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void HandleEndButton(ActionEvent event) throws IOException{
        // get a handle to the stage
        Stage stage = (Stage) TerminerButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    public void handleSaveButton(ActionEvent event) throws IOException {

        String radioButtonChoice = null;
        if(maleRadio.isSelected()) {
            radioButtonChoice = "Homme";
        } else if(femaleRadio.isSelected()) {
            radioButtonChoice = "Femme";
        }

        if (fullNameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must insert all fields (required) !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        } else {

            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
                String query = "INSERT INTO orders VALUES ('" + fullNameField.getText() + "' , " + phoneField.getText() +
                        " , '" + emailField.getText() +  "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetScene();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("You successfully changed the client's contact information");
            alert.setContentText("Click Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            alert.showAndWait();
        }
    }
    public void resetScene() {

        fullNameField.clear();
        phoneField.clear();
        emailField.clear();
        maleRadio.setSelected(false);
    }

}

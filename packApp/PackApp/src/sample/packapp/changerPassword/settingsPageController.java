package sample.packapp.changerPassword;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.packapp.loginPage.rootPageController;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class settingsPageController extends rootPageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField currentPassField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField confirmPassField;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void confirm(ActionEvent event) throws IOException {

        String currentPassword= currentPassField.getText();
        String newPassword = newPassField.getText();
        String confirmPassword = confirmPassField.getText();

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../loginPage/rootPage.fxml"));
        root = loader1.load();
        rootPageController pageController = loader1.getController();

        if(!newPassword.matches("")){
            if(!pageController.displayCurrentpassword().equals(currentPassword)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("The current password is wrong");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                currentPassField.setText("");
                confirmPassField.setText("");
                newPassField.setText("");
            }
            else if(!newPassword.equals(confirmPassword)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("The new password don't match to confirm password");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                currentPassField.setText("");
                confirmPassField.setText("");
                newPassField.setText("");
            }else{
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/packappdb","root","");
                    String query = "UPDATE users SET password = '" + newPassword + "' , oldPassword = '" + currentPassword +"'";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                oldPassword = password;
                password = newPassword;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation!");
                alert.setHeaderText("Password updated successfully!");
                alert.setContentText("Click Ok");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_Info_728979.png");
                stage.getIcons().add(myIcone);
                alert.showAndWait();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Password can not be empty");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        }
    }
}

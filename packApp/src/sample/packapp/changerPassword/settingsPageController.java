package sample.packapp.changerPassword;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.packapp.loginPage.rootPageController;

import java.io.IOException;
import java.sql.*;

public class settingsPageController extends rootPageController {

    private Parent root;

    @FXML
    private Button confirm,cancel;
    @FXML
    private AnchorPane container;
    @FXML
    private PasswordField currentPassField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField confirmPassField;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        Scene scene = cancel.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
    }

    public void confirm(ActionEvent event) throws IOException {

        String currentPassword= currentPassField.getText();
        String newPassword = newPassField.getText();
        String confirmPassword = confirmPassField.getText();

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../loginPage/rootPage.fxml"));
        root = loader1.load();

        if(!newPassword.matches("")){
            if(!displayCurrentpassword().equals(currentPassword)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("The current password is wrong");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                currentPassField.clear();
                confirmPassField.clear();
                newPassField.clear();
            }
            else if(currentPassword.equals(newPassword)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ERROR!");
                alert.setHeaderText("You can not change your password to your current password");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/warning.png");
                stage.getIcons().add(myIcone);
                currentPassField.clear();
                confirmPassField.clear();
                newPassField.clear();
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
                currentPassField.clear();
                confirmPassField.clear();
                newPassField.clear();
            }else{
                try {
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
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
                Scene scene = confirm.getScene();
                root.translateXProperty().set(scene.getWidth());
                StackPane parentContainer = (StackPane) scene.getRoot();
                parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event1 -> {
                    parentContainer.getChildren().remove(container);
                });
                timeline.play();
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

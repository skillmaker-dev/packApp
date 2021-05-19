package sample.packapp.resetPassword;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.packapp.loginPage.rootPageController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class ResetPasswordController extends rootPageController {

    @FXML
    private AnchorPane container;
    @FXML
    private Button check,returnToMainPage;
    @FXML
    private TextField emailInput;
    @FXML

    private String email;


    public String displayEmail(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                email =resultSet.getString("email");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Connection Failed!!");
            alert.setContentText("Click Ok to try again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            return "ConnectionFailed";
        }

        return email;
    }

    public void check(ActionEvent event) throws IOException {

        if(!displayEmail().equals("ConnectionFailed")){
            if(emailInput.getText().equals(displayEmail())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information!");
                alert.setHeaderText("Your Password Is : " + displayCurrentpassword());
                alert.setContentText("Click Ok");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_Info_728979.png");
                stage.getIcons().add(myIcone);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("../loginPage/rootPage.fxml"));
                Parent root = loader.load();
                Scene scene = check.getScene();
                root.translateYProperty().set(scene.getWidth());
                StackPane parentContainer = (StackPane) scene.getRoot();
                parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event1 -> {
                    parentContainer.getChildren().remove(container);
                });
                timeline.play();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Email does not exist!!");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
            }
        }

    }

    public void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../loginPage/rootPage.fxml"));
        Parent root = loader.load();
        Scene scene = returnToMainPage.getScene();
        root.translateYProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
    }
}

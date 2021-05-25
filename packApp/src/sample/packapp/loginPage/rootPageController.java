package sample.packapp.loginPage;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class rootPageController {

    @FXML
    private Button forgetPassword;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane container;
    @FXML
    private TextField textInput;
    @FXML
    private PasswordField passwordInput;

    protected static String password;
    protected static String oldPassword;

    public String displayCurrentpassword() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                password =resultSet.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return password;
    }

    public String displayOldpassword() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                oldPassword =resultSet.getString("oldpassword");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return oldPassword;
    }

    public void login(ActionEvent event ) throws IOException {
        String username = textInput.getText();
        String password = passwordInput.getText();

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM users WHERE username = '"+username+"' and password = '"+password+"'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../mainPage/mainPage.fxml")));
                Scene scene = loginButton.getScene();
                root.translateYProperty().set(scene.getWidth());
                StackPane parentContainer = (StackPane) scene.getRoot();
                parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(event1 -> {
                    parentContainer.getChildren().remove(container);
                });
                timeline.play();
            } else if(passwordInput.getText().equals("") || textInput.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("You must fill all the fields");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                textInput.setText("");
                passwordInput.setText("");
            } else if(displayOldpassword().equals(password)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("You Enter an old password");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                textInput.setText("");
                passwordInput.setText("");
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Wrong Password or Username");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                textInput.setText("");
                passwordInput.setText("");
            }
            connection.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Connection Failed! Check Your Data Base Connection");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            textInput.clear();
            passwordInput.clear();
            e.printStackTrace();
        }
    }

    public void reset(ActionEvent e){
        textInput.clear();
        passwordInput.clear();
    }

    public void resetPassword(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../resetPassword/resetPassword.fxml")));
        Scene scene = forgetPassword.getScene();
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

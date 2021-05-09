package sample.packapp.loginPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;

public class rootPageController {
    @FXML
    private TextField textInput;
    @FXML
    private PasswordField passwordInput;
    private Stage stage;
    private Scene scene;
    private Parent root;

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
                oldPassword =resultSet.getString("oldPassword");
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
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
            alert.setHeaderText("Connection Failed! Check Your Internet Connection");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            textInput.setText("");
            passwordInput.setText("");
        }
    }

    public void reset(ActionEvent e){
        textInput.clear();
        passwordInput.clear();
    }

    public void resetPassword(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resetPassword/resetPassword.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

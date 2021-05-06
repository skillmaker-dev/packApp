package sample.packapp.resetPassword;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.packapp.loginPage.rootPageController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class ResetPasswordController extends rootPageController{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField emailInput;
    @FXML
    private Label myLabel50;

    private String email;


    public String displayEmail(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/packappdb","root","");
            String query = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                email =resultSet.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return email;
    }

    public void check(ActionEvent event) throws IOException {

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
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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

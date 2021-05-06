package sample.packapp.nouvelleCommande;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NouvelleComandeController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private ChoiceBox<String> statusBox;
    @FXML
    private ChoiceBox<String> productBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField amountField;

    private String[] status = {"En cours" , "Livrée" , "Annulée"};
    private String[] products = {"PC Bureau" , "PC Portable" , "Tablette" , "Téléphone" , "TV"};

    /*public void productsList() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/packappdb","root","");
            String query;
            Statement statement = connection.createStatement();
            for(int i = 0; i < products.length; i++) {
                query = "INSERT INTO products VALUES ('" + products[i] + "' , " + 400 + ")";
                statement.executeUpdate(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleSaveButton(ActionEvent event) throws IOException {

        String radioButtonChoice = null;
        if(maleRadio.isSelected()) {
            radioButtonChoice = "Homme";
        } else if(femaleRadio.isSelected()) {
            radioButtonChoice = "Femme";
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/packappdb","root","");
            String query = "INSERT INTO commandes VALUES ('" + fullNameField.getText() + "' , " + phoneField.getText() +
                    " , '" + emailField.getText() + "' , '" + addressField.getText() + "' , '" + radioButtonChoice +
                    "' , '" + productBox.getValue() + "' , " + priceField.getText() + " , " + amountField.getText() +
                    " , '" + statusBox.getValue() + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void handleIncrementButton(ActionEvent event) {

        String amount = amountField.getText();
        int i = Integer.parseInt(amount);
        i += 1;
        amount = Integer.toString(i);
        amountField.setText(amount);

    }

    public void setProductPrice() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/packappdb","root","");
            String query = "SELECT * FROM products WHERE product = '" + productBox.getValue() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.equals(3000.0)) {
                priceField.setText(resultSet.getString("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        statusBox.getItems().addAll(status);
        statusBox.setValue("Status : ");
        productBox.getItems().addAll(products);
        productBox.setValue("Product : ");
        amountField.setText("0");

        //setProductPrice();

    }
}

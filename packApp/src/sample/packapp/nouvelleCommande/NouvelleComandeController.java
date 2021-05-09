package sample.packapp.nouvelleCommande;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private Label totalLabel;

    private String[] status = {"En cours" , "Livrée" , "Annulée"};
    private ObservableList<String> products = FXCollections.observableArrayList();

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void fillProductsBox() {

        String query = "SELECT * FROM products";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                products.add(resultSet.getString("productName"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public NouvelleComandeController() {
        fillProductsBox();
    }

    public void handleSaveButton(ActionEvent event) throws IOException {

        String radioButtonChoice = null;
        if(maleRadio.isSelected()) {
            radioButtonChoice = "Homme";
        } else if(femaleRadio.isSelected()) {
            radioButtonChoice = "Femme";
        }

        if (fullNameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
        || addressField.getText().isEmpty() || priceField.getText().isEmpty() || radioButtonChoice == null ||
        productBox.getValue().contentEquals("Product : ") || statusBox.getValue().contentEquals("Status : ")) {
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
                String query = "INSERT INTO commandes VALUES ('" + fullNameField.getText() + "' , " + phoneField.getText() +
                        " , '" + emailField.getText() + "' , '" + addressField.getText() + "' , '" + radioButtonChoice +
                        "' , '" + productBox.getValue() + "' , " + priceField.getText() + " , " + amountField.getText() +
                        " , '" + statusBox.getValue() + "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetScene();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("You successfully added a new command!");
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
        addressField.clear();
        priceField.clear();
        amountField.clear();
        statusBox.getItems().addAll(status);
        statusBox.setValue("Status : ");
        productBox.getItems().addAll(products);
        productBox.setValue("Product : ");
        amountField.setText("0");
        maleRadio.setSelected(false);

    }

    public void handleIncrementButton(ActionEvent event) {

        String amount = amountField.getText();
        int i = Integer.parseInt(amount);
        i += 1;
        amount = Integer.toString(i);
        amountField.setText(amount);
        double total=0.0;
        total = Integer.parseInt(amount)*getProductPrice();
        String totalPrice;
        totalPrice= Double.toString(total);
        totalLabel.setText(totalPrice);
    }

    public void handleDecrementButton(ActionEvent event) {

        String amount = amountField.getText();
        int i = Integer.parseInt(amount);

        if(i>0)
        {
            i=i-1;
        }

        amount = Integer.toString(i);
        amountField.setText(amount);
        double total=0.0;
        total = Integer.parseInt(amount)*getProductPrice();
        String totalPrice;
        totalPrice= Double.toString(total);
        totalLabel.setText(totalPrice);
    }

    public double getProductPrice(){
        double price = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("productName").equals(productBox.getValue())){
                    price = resultSet.getDouble("unitPrice");
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    public void setProductPrice(ActionEvent event) {

        priceField.setText(Double.toString(getProductPrice()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        statusBox.getItems().addAll(status);
        statusBox.setValue("Status : ");
        productBox.getItems().addAll(products);
        productBox.setValue("Product : ");
        amountField.setText("0");
        totalLabel.setText("0");
        productBox.setOnAction(this::setProductPrice);

    }

}

package sample.packapp.nouvelleCommande;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NouvelleComandeController implements Initializable {

    @FXML
    private AnchorPane container;
    @FXML
    private Button retour,save;
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
    @FXML
    private Button incrementButton,decrementButton;

    private String[] status = {"En cours" , "Livrée" , "Annulée"};
    private ObservableList<String> products = FXCollections.observableArrayList();
    ReferenceGenerator refGen;
    IdGenerator idGen;

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        Parent root = loader.load();
        Scene scene = retour.getScene();
        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(container);
        });
        timeline.play();
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

    public boolean checkClientExist() {
        boolean check = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            String query = "SELECT * FROM clients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("name").equals(fullNameField.getText())){
                    check = true;
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;

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
                Connection connection1 = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
                Statement statement = connection1.createStatement();
                String query1 = "SELECT * FROM products WHERE productName = '" + productBox.getValue() + "'";
                ResultSet resultSet = statement.executeQuery(query1);
                int selectedProductQuantity = 0;
                while (resultSet.next()) {
                    selectedProductQuantity = resultSet.getInt("quantity");
                }
                if (selectedProductQuantity == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText("Product out of stock !");
                    alert.setContentText("Click Ok to Continue");
                    alert.show();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                    stage.getIcons().add(myIcone);
                } else if((selectedProductQuantity - Integer.parseInt(amountField.getText()) < 0)) {
                     if(Integer.parseInt(amountField.getText()) > selectedProductQuantity) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR!");
                        alert.setHeaderText("It left less than you desire from this product , Please decrease the amount !");
                        alert.setContentText("Click Ok to Continue");
                        alert.show();
                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                        stage.getIcons().add(myIcone);
                    }
                } else {
                    String productRef = refGen.generateRef();
                    String clientId = idGen.generateId();
                    int nbrOfOrders = 0;
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
                    String query = "INSERT INTO orders VALUES ('" + fullNameField.getText() + "' , " + phoneField.getText() +
                            " , '" + emailField.getText() + "' , '" + addressField.getText() + "' , '" + radioButtonChoice +
                            "' , '" + productBox.getValue() + "' , " + priceField.getText() + " , " + amountField.getText() +
                            " , '" + statusBox.getValue() + "' , '" + productRef + "')";
                    Statement statement1 = connection.createStatement();
                    statement1.executeUpdate(query);
                    String query2 = "UPDATE products SET quantity = " + (selectedProductQuantity - Integer.parseInt(amountField.getText()))
                            + " WHERE productName = '" + productBox.getValue() + "'";
                    statement.executeUpdate(query2);
                    if(!checkClientExist()) {
                        String query3 = "INSERT INTO clients VALUES ('" + clientId + "' , '" + fullNameField.getText()
                                + "' , '" + productRef + "' , " + 1 + ")";
                        Statement statement2 = connection.createStatement();
                        statement2.executeUpdate(query3);
                    } else {
                        String query3 = "SELECT nbrOrders FROM clients WHERE name = '" + fullNameField.getText() + "'";
                        Statement statement2 = connection.createStatement();
                        ResultSet resultSet1 = statement2.executeQuery(query3);
                        while (resultSet1.next()) {
                            nbrOfOrders = resultSet1.getInt("nbrOrders") + 1;
                        }
                        String query4 = "UPDATE clients SET nbrOrders = " + nbrOfOrders + " , lastOrder  = '" +
                                productRef + "' WHERE name = '" + fullNameField.getText() + "'";
                        Statement statement3 = connection.createStatement();
                        statement3.executeUpdate(query4);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void resetScene() {

        fullNameField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        priceField.clear();
        amountField.clear();
        statusBox.setValue("Status : ");
        productBox.setValue("Product : ");
        amountField.setText("0");
        totalLabel.setText("0");
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
        incrementButton.setDisable(false);
        decrementButton.setDisable(false);
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
        incrementButton.setDisable(true);
        decrementButton.setDisable(true);
        productBox.setOnAction(this::setProductPrice);

    }

}

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
import javafx.scene.Node;
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
    private TextField idfield;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
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
    @FXML
    private Button generateIdButton;
    @FXML
    private Button refreshButton;

    private String[] status = {"In progress" , "Delivered" , "Canceled"};
    private ObservableList<String> products = FXCollections.observableArrayList();
    IdGenerator idGen;
    int orderID = 0;
    double totalPrice = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        orderID = idGen.generateId();
        statusBox.getItems().addAll(status);
        statusBox.setValue("Status : ");
        productBox.getItems().addAll(products);
        productBox.setValue("Product : ");
        amountField.setText("0");
        totalLabel.setText("0");
        incrementButton.setDisable(true);
        decrementButton.setDisable(true);
        save.setDisable(true);
        priceField.setEditable(false);
        productBox.setOnAction(this::setProductPrice);

    }

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        Parent root = loader.load();
        Scene scene = retour.getScene();
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

    public void fillProductsBox() {

        String query = "SELECT * FROM products";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                products.add(resultSet.getString("name"));
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
                if(resultSet.getString("client_id").equals(idfield.getText())){
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

    public void resetScene() {

        idfield.clear();
        idfield.setEditable(true);
        fullNameField.clear();
        fullNameField.setEditable(true);
        phoneField.clear();
        phoneField.setEditable(true);
        emailField.clear();
        emailField.setEditable(true);
        addressField.clear();
        addressField.setEditable(true);
        priceField.clear();
        priceField.setEditable(true);
        amountField.clear();
        amountField.setEditable(true);
        statusBox.setValue("Status : ");
        statusBox.setDisable(false);
        productBox.setValue("Product : ");
        amountField.setText("0");
        totalLabel.setText("0");
        generateIdButton.setDisable(false);
        refreshButton.setDisable(false);
        save.setDisable(true);


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
                if(resultSet.getString("name").equals(productBox.getValue())){
                    price = resultSet.getDouble("unit_price");
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

    public void handleSaveButton(ActionEvent event) throws IOException {

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            if(!checkClientExist()) {
                String query = "INSERT INTO clients VALUES (" + Integer.parseInt(idfield.getText()) + " , '" + fullNameField.getText()
                        + "' , '" +  phoneField.getText() + "' , '" + emailField.getText() + "' , '" + addressField.getText()
                        + "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                String query1 = "INSERT INTO orders VALUES (" + orderID + " , " + Integer.parseInt(idfield.getText()) + " , '" + statusBox.getValue()
                        + "' , " + totalPrice + ")";
                Statement statement1 = connection.createStatement();
                statement1.executeUpdate(query1);
            } else {
                String query1 = "INSERT INTO orders VALUES (" + orderID + " , " + Integer.parseInt(idfield.getText()) + " , '" + statusBox.getValue()
                        + "' , " + totalPrice + ")";
                Statement statement1 = connection.createStatement();
                statement1.executeUpdate(query1);
            }
            connection.close();
            resetScene();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("You successfully added a new order!");
            alert.setContentText("Click Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        resetScene();
        orderID = idGen.generateId();
    }

    public void handleAddToCartButton(ActionEvent event) {

        if (idfield.getText().isEmpty() || fullNameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()
                || addressField.getText().isEmpty() || priceField.getText().isEmpty() ||
                productBox.getValue().contentEquals("Product : ") || statusBox.getValue().contentEquals("Status : ")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must insert all fields (required) !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        } else if(amountField.getText().equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You did not determine the desired amount of the product !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        } else {
            try {
                Connection connection1 = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
                Statement statement1 = connection1.createStatement();
                String query1 = "SELECT * FROM products WHERE name = '" + productBox.getValue() + "'";
                ResultSet resultSet1 = statement1.executeQuery(query1);
                int selectedProductQuantity = 0;
                int selectedProductId = 0;
                while (resultSet1.next()) {
                    selectedProductQuantity = resultSet1.getInt("quantity");
                    selectedProductId = resultSet1.getInt("product_id");
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
                    int boughtQuantity = 0;
                    boughtQuantity = Integer.parseInt(amountField.getText());
                    String query2 = "INSERT INTO order_items VALUES ( " + orderID + " , " + selectedProductId + " , " +
                            boughtQuantity + " , " + Double.parseDouble(priceField.getText()) + ')';
                    Statement statement2 = connection1.createStatement();
                    String query3 = " UPDATE products SET quantity = quantity - " + boughtQuantity + " WHERE product_id = " + selectedProductId +   "";
                    totalPrice += Double.parseDouble(priceField.getText());
                    statement2.executeUpdate(query2);
                    statement2.executeUpdate(query3);
                    idfield.setEditable(false);
                    fullNameField.setEditable(false);
                    phoneField.setEditable(false);
                    emailField.setEditable(false);
                    addressField.setEditable(false);
                    statusBox.setDisable(true);
                    save.setDisable(false);
                    generateIdButton.setDisable(true);
                    refreshButton.setDisable(true);
                    incrementButton.setDisable(true);
                    decrementButton.setDisable(true);
                    priceField.clear();
                    amountField.clear();
                    productBox.setValue("Product : ");
                    amountField.setText("0");
                    totalLabel.setText("0");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmation!");
                    alert.setHeaderText("Product successfully added to cart. You can add a new product to your cart or save the current order");
                    alert.setContentText("Click Ok to continue");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image myIcone = new Image("sample/icon/iconfinder_Info_728979.png");
                    stage.getIcons().add(myIcone);
                    alert.showAndWait();
                    connection1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void refreshButton(ActionEvent event) {

        resetScene();

    }

    public void generateIdButton(ActionEvent event) {
        idfield.setText(Integer.toString(idGen.generateId()));
    }
}

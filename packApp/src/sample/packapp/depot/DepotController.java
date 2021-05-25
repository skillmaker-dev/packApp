package sample.packapp.depot;


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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class DepotController<root> implements Initializable {

    @FXML
    private AnchorPane container;
    @FXML
    private Button retour,modifier,supprimer,ajouter;

    private int ID;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField unitPriceTextField;
    @FXML
    private TableView<Products> productsTableView;
    @FXML
    private TableColumn<Products,Integer> idColumn;
    @FXML
    private TableColumn<Products,String> productNameColumn;
    @FXML
    private TableColumn<Products,Integer> quantityColumn;
    @FXML
    private TableColumn<Products,Double> unitPriceColumn;
    @FXML
    private Button qrbutton;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProducts();
        idTextField.setEditable(false);
        modifier.setDisable(true);
        supprimer.setDisable(true);
        qrbutton.setDisable(true);
    }

    public void qrStage(ActionEvent event) {


        String id = "Product id : " + idTextField.getText();
        String name = "Product Name : " + productNameTextField.getText();
        String quantity = "Quantity : " + quantityTextField.getText();
        String price = "Product Price : " + unitPriceTextField.getText();

        String Product = id + "\n" + name + "\n" + quantity + "\n" + price;

        if(idTextField.getText().isEmpty() || productNameTextField.getText().isEmpty() || quantityTextField.getText().isEmpty() || unitPriceTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must select a product to generate its QR Code !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QrCode.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                QrCodeGeneratorController qrCodeGeneratorController = fxmlLoader.getController();
                qrCodeGeneratorController.generate(Product);
                Stage stage = new Stage();
                stage.setTitle("Code Qr");
                stage.setResizable(false);
                Image myIcon = new Image("sample/icon/PackageAPP.png");
                stage.getIcons().add(myIcon);
                stage.setScene(new Scene(root1));
                stage.centerOnScreen();
                stage.show();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    public Connection getConnection() {

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:packApp/src/sample/DataBase/sqlite.db");
            return connection;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    public ObservableList<Products> getProductsList() {

        ObservableList<Products> productsList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM products";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Products products;
            while (resultSet.next()) {
                products = new Products(resultSet.getInt("product_id"),resultSet.getString("name"),resultSet.getInt("quantity"),resultSet.getDouble("unit_price"));
                productsList.add(products);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

    public void showProducts() {

        ObservableList<Products> list = getProductsList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Products,Integer>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Products,String>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Products,Integer>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<Products,Double>("unitPrice"));

        productsTableView.setItems(list);
    }

    private void executeQuery(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFields() {
        idTextField.clear();
        productNameTextField.clear();
        quantityTextField.clear();
        unitPriceTextField.clear();
    }

    public void handleInsertProducts() throws SQLException {

        if (!quantityTextField.getText().matches("[0-9]*") || !unitPriceTextField.getText().matches("[0-9]*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must enter valide value !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            clearFields();
        } else if (productNameTextField.getText().equals("") || quantityTextField.getText().equals("") || unitPriceTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must insert all fields (required) !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            clearFields();
        } else {
            String query = "INSERT INTO products (name, quantity, unit_price) VALUES ('" + productNameTextField.getText() + "' , " + quantityTextField.getText() + " , " + unitPriceTextField.getText() + ")";
            executeQuery(query);
            showProducts();
            clearFields();
        }
    }

    public void handleUpdateProducts() {
        if(!quantityTextField.getText().matches("[0-9]*")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must enter valid value !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            clearFields();
        }else{
            if(productNameTextField.getText().isEmpty() || quantityTextField.getText().isEmpty() || unitPriceTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("You must select a product to UPDATE it !");
                alert.setContentText("Click Ok to Try Again");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
            }
            else{
                String query = "UPDATE products SET name = '" + productNameTextField.getText() + "' , quantity = " + quantityTextField.getText() + " , unit_price = " + unitPriceTextField.getText() + " WHERE product_id = " + idTextField.getText() + "";
                executeQuery(query);
                showProducts();
                clearFields();
                modifier.setDisable(true);
                supprimer.setDisable(true);
            }
        }
    }

    public void handleDeleteProducts() {
        if(productNameTextField.getText().isEmpty() || quantityTextField.getText().isEmpty() || unitPriceTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must select a product to DELETE it !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
        }else{
            String query = "DELETE FROM products WHERE product_id = " + idTextField.getText() + "";
            executeQuery(query);
            showProducts();
            clearFields();
            modifier.setDisable(true);
            supprimer.setDisable(true);
        }
    }

    @FXML
    private void handleMouseAction(MouseEvent event) {

        if(!productsTableView.getSelectionModel().isEmpty()){
            Products products = productsTableView.getSelectionModel().getSelectedItem();
            idTextField.setText("" + products.getId());
            productNameTextField.setText("" + products.getProductName());
            quantityTextField.setText("" + products.getQuantity());
            unitPriceTextField.setText("" + products.getUnitPrice());
            modifier.setDisable(false);
            supprimer.setDisable(false);
            qrbutton.setDisable(false);
            qrbutton.setDisable(false);
            ajouter.setDisable(true);
            idTextField.setEditable(false);
        }
    }

    public void refresh(ActionEvent event) {
        showProducts();
        idTextField.setEditable(false);
        modifier.setDisable(true);
        supprimer.setDisable(true);
        qrbutton.setDisable(true);
        ajouter.setDisable(false);
        clearFields();
    }
}

package sample.packapp.depot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DepotController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

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

    public void mainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/mainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showProducts();

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
                products = new Products(resultSet.getInt("id"),resultSet.getString("productName"),resultSet.getInt("quantity"),resultSet.getDouble("unitPrice"));
                productsList.add(products);
            }
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

    public void handleInsertProducts() {

        if (idTextField.getText().equals("") || productNameTextField.getText().equals("") || quantityTextField.getText().equals("") || unitPriceTextField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("You must insert all fields (required) !");
            alert.setContentText("Click Ok to Try Again");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("sample/icon/iconfinder_sign-error_299045.png");
            stage.getIcons().add(myIcone);
            clearFields();
        }
        else {
            String query = "INSERT INTO products VALUES (" + idTextField.getText() + " , '" + productNameTextField.getText() + "' , " + quantityTextField.getText() + " , " + unitPriceTextField.getText() + ")";
            executeQuery(query);
            showProducts();
            clearFields();
        }

    }

    public void handleUpdateProducts() {

        String query = "UPDATE products SET id = " + idTextField.getText() + " , productName = '" + productNameTextField.getText() + "' , quantity = " + quantityTextField.getText() + " , unitPrice = " + unitPriceTextField.getText() + " WHERE id = " + idTextField.getText() + "";
        executeQuery(query);
        showProducts();
        clearFields();

    }

    public void handleDeleteProducts() {

        String query = "DELETE FROM products WHERE id = " + idTextField.getText() + "";
        executeQuery(query);
        showProducts();
        clearFields();

    }

    @FXML
    private void handleMouseAction(MouseEvent event) {

        Products products = productsTableView.getSelectionModel().getSelectedItem();
        idTextField.setText("" + products.getId());
        productNameTextField.setText("" + products.getProductName());
        quantityTextField.setText("" + products.getQuantity());
        unitPriceTextField.setText("" + products.getUnitPrice());

    }

}

package sample.packapp.clients.AffichageDesCommandes;

public class OrderItems {

    private int productId;
    private String productName;
    private Double price;
    private int quantity;

    public OrderItems(int productId, String productName, Double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

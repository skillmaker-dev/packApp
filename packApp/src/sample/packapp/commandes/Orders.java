package sample.packapp.commandes;

public class Orders {

    private int orderId;
    private String fullName;
    private int clientId;
    private double totalPrice;
    private String status;

    public Orders(int orderId,  int clientId, String fullName, double totalPrice, String status) {
        this.orderId = orderId;
        this.fullName = fullName;
        this.clientId = clientId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getClientId() {
        return clientId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }
}

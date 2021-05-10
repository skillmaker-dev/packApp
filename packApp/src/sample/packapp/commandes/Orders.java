package sample.packapp.commandes;

public class Orders {

    private String reference;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String product;
    private double price;
    private int amount;
    private String status;


    public Orders(String reference, String fullName, String phone, String email, String address, String gender,
                  String product, double price, int amount, String status) {
        this.reference = reference;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.product = product;
        this.price = price*amount;


        this.amount = amount;
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}

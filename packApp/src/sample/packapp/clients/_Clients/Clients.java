package sample.packapp.clients._Clients;

public class Clients {
    private int clientId;
    private String clientName;
    private String phone;
    private String email;
    private String address;

    public Clients(int clientId, String clientName, String phone, String email, String address) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
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
}

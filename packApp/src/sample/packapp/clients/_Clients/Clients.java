package sample.packapp.clients._Clients;

public class Clients {
    private int idClient;
    private String ClientName;
    private String lastCmd;
    private int NbCmd;

    public Clients(int idClient, String ClientName, String lastCmd, int NbCmd) {
        this.idClient = idClient;
        this.ClientName = ClientName;
        this.lastCmd = lastCmd;
        this.NbCmd = NbCmd;
    }

    public int getNumClient() {
        return idClient;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getlastCmd() {
        return lastCmd;
    }

    public double getNbCmd() {
        return NbCmd;
    }
}

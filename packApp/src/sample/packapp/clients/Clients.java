package sample.packapp.clients;

public class Clients {
    private int numClient;
    private String ClientName;
    private String lastCmd;
    private int NbCmd;

    public Clients(int numClient, String ClientName, String lastCmd, int NbCmd) {
        this.numClient = numClient;
        this.ClientName = ClientName;
        this.lastCmd = lastCmd;
        this.NbCmd = NbCmd;
    }

    public int getNumClient() {
        return numClient;
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

package sample.packapp.clients._Clients;

public class Clients {
    private int clientId;
    private String clientName;
    private String lastCmd;
    private int NbCmd;

    public Clients(int clientId, String clientName, String lastCmd, int nbCmd) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.lastCmd = lastCmd;
        NbCmd = nbCmd;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getLastCmd() {
        return lastCmd;
    }

    public int getNbCmd() {
        return NbCmd;
    }
}

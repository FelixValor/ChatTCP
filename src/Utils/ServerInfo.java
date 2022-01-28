package Utils;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;

public class ServerInfo implements Serializable {
    private int MAXIMUM, CONECTIONSAMOUNT;
    public ArrayList<Socket> clientsSockets;

    public ServerInfo(){}

    public ServerInfo(int MAXIMUM, int CONECTIONSAMOUNT, ArrayList<Socket> clientsSockets) {
        this.MAXIMUM = MAXIMUM;
        this.CONECTIONSAMOUNT = CONECTIONSAMOUNT;
        this.clientsSockets = clientsSockets;
    }

    public int getMAXIMUM() {
        return MAXIMUM;
    }

    public void setMAXIMUM(int MAXIMUM) {
        this.MAXIMUM = MAXIMUM;
    }

    public int getCONECTIONSAMOUNT() {
        return CONECTIONSAMOUNT;
    }

    public void setCONECTIONSAMOUNT(int CONECTIONSAMOUNT) {
        this.CONECTIONSAMOUNT = CONECTIONSAMOUNT;
    }

    public ArrayList<Socket> getClientsSockets() {
        return clientsSockets;
    }

    public void setClientsSockets(ArrayList<Socket> clientsSockets) {
        this.clientsSockets = clientsSockets;
    }

    public void addClientSocket(Socket clientSocket, int position) {
        this.clientsSockets.add(clientSocket);
    }
}

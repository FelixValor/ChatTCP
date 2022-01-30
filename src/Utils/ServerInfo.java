package Utils;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;

public class ServerInfo implements Serializable {
    private int MAXIMUM;
    public static ArrayList<Socket> clientsSockets;

    public ServerInfo(){}

    public ServerInfo(int MAXIMUM, ArrayList<Socket> clientsSocket) {
        this.MAXIMUM = MAXIMUM;
        ServerInfo.clientsSockets = clientsSocket;
    }

    public int getMAXIMUM() {
        return MAXIMUM;
    }

    public void setMAXIMUM(int MAXIMUM) {
        this.MAXIMUM = MAXIMUM;
    }

    public int getCONECTIONSAMOUNT() {
        return clientsSockets.size();
    }

    public ArrayList<Socket> getClientsSockets() {
        return clientsSockets;
    }

    public void setClientsSockets(ArrayList<Socket> clientsSockets) {
        ServerInfo.clientsSockets = clientsSockets;
    }

    public void addClientSocket(Socket clientSocket) {
        ServerInfo.clientsSockets.add(clientSocket);
    }
}

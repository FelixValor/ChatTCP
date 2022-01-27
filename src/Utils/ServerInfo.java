package Utils;

import java.net.Socket;

public class ServerInfo {
    private int MAXIMUM, CONECTIONSAMOUNT;
    public Socket[] clientsSockets;

    public ServerInfo(){}

    public ServerInfo(int MAXIMUM, int CONECTIONSAMOUN, Socket[] clientsSockets) {
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

    public Socket[] getClientsSockets() {
        return clientsSockets;
    }

    public void setClientsSockets(Socket[] clientsSockets) {
        this.clientsSockets = clientsSockets;
    }

    public void addClientSocket(Socket clientSocket, int position) {
        this.clientsSockets[position] = clientSocket;
    }
}

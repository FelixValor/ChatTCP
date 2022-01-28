package Utils;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class ClientInfo implements Serializable {

    private ArrayList<String> currentsClients;
    private String message, targetIp;
    private Integer  targetPort;

    public ClientInfo(ArrayList<String> currentsClients, String message, String targetIp, Integer targetPort) {
        this.currentsClients = currentsClients;
        this.message = message;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
    }

    public ArrayList<String> getCurrentsClients() {
        return currentsClients;
    }

    public void setCurrentsClients(ArrayList<String> currentsClients) {
        this.currentsClients = currentsClients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }
}

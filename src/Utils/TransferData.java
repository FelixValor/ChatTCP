package Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class TransferData implements Serializable {

    private ArrayList<Integer> currentsClients;
    private String message;
    private Integer  clientID, target;

    public TransferData(ArrayList<Integer> currentsClients, String message, Integer clientID, Integer target) {
        this.currentsClients = currentsClients;
        this.message = message;
        this.clientID = clientID;
        this.target = target;
    }

    public ArrayList<Integer> getCurrentsClients() {
        return currentsClients;
    }

    public void setCurrentsClients(ArrayList<Integer> currentsClients) {
        this.currentsClients = currentsClients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}

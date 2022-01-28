package ServerPackage;

import Utils.TransferData;
import Utils.ServerInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private ServerInfo serverInfo;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;

    public ServerThread(Socket clientSocket, ServerInfo serverInfo) {
        this.clientSocket = clientSocket;
        this.serverInfo = serverInfo;

        try {
            ous = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            System.out.println("ERROR DE E/S");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        TransferData clientInfo;

        try{

            ArrayList<Integer> clientsID = new ArrayList<Integer>();
            for (int i = 0; i < serverInfo.getCONECTIONSAMOUNT(); i++) clientsID.add(i);

            clientInfo = new TransferData(clientsID, null, serverInfo.getCONECTIONSAMOUNT()-1,null);
            ous.reset();
            ous.writeObject(clientInfo);

            while (true){
                TransferData infoFromClient = (TransferData) ois.readObject();

                try{
                    serverInfo.getClientsSockets().get(infoFromClient.getTarget());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.err.println("Error al comunicarse con el cliente: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

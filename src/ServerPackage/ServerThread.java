package ServerPackage;

import Utils.TransferData;
import Utils.ServerInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private final ServerInfo serverInfo;
    private ObjectOutputStream ous;
    private ObjectInputStream ois;

    public ServerThread(Socket clientSocket, ServerInfo serverInfo) {
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
        Integer id = serverInfo.getCONECTIONSAMOUNT()-1;

        try{

            ArrayList<Integer> clientsID = new ArrayList<Integer>();
            for (int i = 0; i < serverInfo.getCONECTIONSAMOUNT(); i++) clientsID.add(i);

            //Enviamos la informacion inicial al cliente
            clientInfo = new TransferData(clientsID, null, serverInfo.getCONECTIONSAMOUNT()-1,null);
            ous.reset();
            ous.writeObject(clientInfo);

            while (true){
                TransferData infoFromClient = (TransferData) ois.readObject();

                //Se procesa la informacion recibida y se lo manda al destinatario
                try{
                    ObjectOutputStream ous = new ObjectOutputStream(ServerInfo.clientsSockets.get(infoFromClient.getTarget()).getOutputStream());
                    ous.reset();
                    clientsID.clear();
                    for (int i = 0; i < serverInfo.getCONECTIONSAMOUNT(); i++) clientsID.add(i);
                    ous.reset();
                    TransferData infoToClient = new TransferData(clientsID, infoFromClient.getMessage(), infoFromClient.getTarget(), infoFromClient.getClientID());
                    ous.writeObject(infoToClient);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.err.println("El cliente "+id+" se ha desconectado");
            serverInfo.deleteSocketByID(id);
        }
    }
}

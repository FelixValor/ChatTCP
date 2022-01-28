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

            ArrayList<String> clients = new ArrayList<String>();
            ArrayList<Socket> listClients = serverInfo.getClientsSockets();
            for (Socket listClient : listClients) {
                if (listClient == null) break;
                clients.add(listClient.getInetAddress().getHostAddress() + ":" + listClient.getPort());
            }

            clientInfo = new TransferData(clients, null, null,null);
            ous.reset();
            ous.writeObject(clientInfo);

            while (true){
                TransferData infoFromClient = (TransferData) ois.readObject();
/*                System.out.println(serverInfo.getClientsSockets()[0].getInetAddress().getHostAddress());
                System.out.println(serverInfo.getClientsSockets()[0].getPort());
                System.out.println("OBJETIVO");
                System.out.println(infoFromClient.getTargetIp());
                System.out.println(infoFromClient.getTargetPort());
                System.out.println(serverInfo.getClientsSockets().length);*/

                try{
                    for (Socket clientTarget : serverInfo.getClientsSockets()) {
                        if (infoFromClient.getTargetIp().equals(clientTarget.getInetAddress().getHostAddress()) && infoFromClient.getTargetPort()==clientTarget.getPort()){
                            ObjectOutputStream ousToTarget = new ObjectOutputStream(clientTarget.getOutputStream());
                            ousToTarget.reset();
                            ousToTarget.writeObject(new TransferData(infoFromClient.getCurrentsClients(), infoFromClient.getMessage(), null, null));
                        }
                    }
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

package ServerPackage;

import Utils.ClientInfo;
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
        ClientInfo clientInfo;

        try{

            ArrayList<String> clients = new ArrayList<String>();
            Socket [] listClients = serverInfo.getClientsSockets();
            for (Socket listClient : listClients) {
                if (listClient == null) break;
                clients.add(listClient.getInetAddress().getHostAddress() + ":" + listClient.getPort());
            }

            clientInfo = new ClientInfo(clients, null, null,null);
            ous.reset();
            ous.writeObject(clientInfo);

            while (true){
                ClientInfo infoFromClient = (ClientInfo) ois.readObject();
                System.out.println(serverInfo.getClientsSockets()[0].getInetAddress().getHostAddress());
                System.out.println(serverInfo.getClientsSockets()[0].getPort());
                System.out.println("OBJETIVO");
                System.out.println(infoFromClient.getTargetIp());
                System.out.println(infoFromClient.getTargetPort());
                System.out.println(serverInfo.getClientsSockets().length);

                try{
                    for (int i=0;serverInfo.getClientsSockets().length>i;i++) {
                        if (infoFromClient.getTargetIp().equals(serverInfo.getClientsSockets()[i].getInetAddress().getHostAddress())&& infoFromClient.getTargetPort()==serverInfo.getClientsSockets()[i].getPort()){
                            ObjectOutputStream ousToTarget = new ObjectOutputStream(serverInfo.getClientsSockets()[i].getOutputStream());
                            ousToTarget.reset();
                            ousToTarget.writeObject(new ClientInfo(infoFromClient.getCurrentsClients(), infoFromClient.getMessage(), infoFromClient.getTargetIp(), infoFromClient.getTargetPort()));
                            System.out.println(i);
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

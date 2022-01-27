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
            for (int i = 0; i < listClients.length; i++) {
                if (listClients[i]==null) break;
                clients.add(listClients[i].getInetAddress().getHostAddress()+":"+listClients[i].getPort());
            }

            clientInfo = new ClientInfo(clients, null, null,null);
            ous.reset();
            ous.writeObject(clientInfo);

            while (true){
                ClientInfo infoFromClient = (ClientInfo) ois.readObject();

                try{
                    for (Socket clientTarget : serverInfo.getClientsSockets()) {
                        if (infoFromClient.getTargetIp().equals(clientTarget.getInetAddress().getHostAddress())&& infoFromClient.getTargetPort()==clientTarget.getPort()){
                            ObjectOutputStream ousToTarget = new ObjectOutputStream(clientTarget.getOutputStream());
                            ousToTarget.reset();
                            ousToTarget.writeObject(new ClientInfo(infoFromClient.getCurrentsClients(), infoFromClient.getMessage(), null, null));
                        }
                    }

                }catch (Exception e){

                }
            }
        }catch (Exception e){
            System.err.println("Error al comunicarse con el cliente: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

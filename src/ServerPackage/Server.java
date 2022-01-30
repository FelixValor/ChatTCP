package ServerPackage;

import Utils.ServerInfo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int MAXCLIENTS = 5;
    private static final int PORT = 4444;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("[+] Servidor Iniciado\n#####################");

            ArrayList<Socket> clientsSockets = new ArrayList<Socket>(0);
            ServerInfo serverInfo = new ServerInfo(MAXCLIENTS, clientsSockets);

            while (serverInfo.getCONECTIONSAMOUNT()<MAXCLIENTS){
                Socket clientSocket = serverSocket.accept();
                System.out.println("entra:"+clientSocket.getPort());

                serverInfo.addClientSocket(clientSocket);

                System.out.println("Cliente Conectado: "+clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort());
                System.out.println("Clientes actuales: "+serverInfo.getCONECTIONSAMOUNT());

                ServerThread serverThread = new ServerThread(clientSocket, serverInfo);
                serverThread.start();

            }
            serverSocket.close();

        }catch (Exception e){
            System.err.println("Server Error: "+e.getMessage());
            e.printStackTrace();
        }
    }

}

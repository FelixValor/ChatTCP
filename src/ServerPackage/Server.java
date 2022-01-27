package ServerPackage;

import Utils.ServerInfo;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int MAXCLIENTS = 5;
    private static final int PORT = 4444;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("[+] Servidor Iniciado\n#####################");

            Socket[] clientsSockets = new Socket[MAXCLIENTS];
            ServerInfo serverInfo = new ServerInfo(MAXCLIENTS, 0, clientsSockets);

            while (serverInfo.getCONECTIONSAMOUNT()<MAXCLIENTS){
                Socket clientSocket = new Socket();
                clientSocket = serverSocket.accept();


                serverInfo.addClientSocket(clientSocket, serverInfo.getCONECTIONSAMOUNT());
                serverInfo.setCONECTIONSAMOUNT(serverInfo.getCONECTIONSAMOUNT()+1);

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

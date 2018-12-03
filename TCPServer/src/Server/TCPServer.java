package Server;

import java.net.*;
import java.io.*;

public class TCPServer {
    
    public static void main (String args[]) {
        
        Banco banco = new Banco(5);
        
        try{
            int serverPort = 5440; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);
            
            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection conect = new Connection(clientSocket, banco.listaContas);
            }
        } catch (IOException e) {System.out.println("Listen socket:"+e.getMessage());}
    }
}
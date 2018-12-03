package Client;

import java.net.*;
import java.io.*;

public class TCPClient {
    
    static String data;
    
    public static void main (String args[]) {
        
        Criptografia TEA = new Criptografia();
        Socket s = null;
        
        try{
            int serverPort = 5440;
            s = new Socket("127.0.0.1", serverPort);
            DataInputStream in = new DataInputStream( s.getInputStream());
            DataOutputStream out = new DataOutputStream( s.getOutputStream());
            
            out.writeUTF(TEA.encrypt(args[0] + "#" + args[1] + "#" + args[2] + "#" + args[3]));
            data = TEA.decrypt(in.readUTF());

        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
}
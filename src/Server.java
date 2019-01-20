import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serv =  new ServerSocket(80);
        Socket sock = null;
        BufferedReader keyboard = null;
        PrintWriter output = null;
        ArrayList activeThreads = new ArrayList();


        while(true){
            //accepting connection
            System.out.println("Awaiting for connection...");
            sock = serv.accept();

            keyboard = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintWriter(sock.getOutputStream());

            ClientHandler clientHandlerThread = new ClientHandler(sock, keyboard, output);
            clientHandlerThread.start();
        }
    }
}

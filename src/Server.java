import java.io.*;
import java.net.*;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serv =  new ServerSocket(80);
        Socket sock = null;
        BufferedReader keyboard = null;
        PrintWriter output = null;


        while(true){
            //accepting connection
            System.out.println("Awaiting for connection...");
            sock = serv.accept();

            keyboard = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintWriter(sock.getOutputStream());

            new ClientHandler(sock, keyboard, output).start();
        }
    }
}

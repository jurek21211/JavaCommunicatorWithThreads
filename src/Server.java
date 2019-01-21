import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    public static ArrayList<Socket> clientList = new ArrayList<Socket>();

    public static void main(String args[]) throws IOException{
        ServerSocket serv = new ServerSocket(80);

        while(true){
            System.out.println("Waiting for connection...");
            Socket sock = serv.accept();

            new TaskHandler(sock).start();
        }
    }

    private  class Sender extends Thread{
        String message = null;

        Sender(String message){
            this.message = message;
        }

        public void run(){
            try{
                
            }
        }
    }
}

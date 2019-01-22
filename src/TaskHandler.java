import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TaskHandler extends Thread {
    Socket sock = null;
    BufferedReader reader;
    String message = null;
    Server server = null;


    TaskHandler(Socket clientSocket){
        try {
            this.sock = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){

        try {

            while (true) {
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Connection terminated");
                    reader.close();
                    break;
                }else{
                    message = reader.readLine();
                    System.out.println("<"+this.sock.getLocalPort()+"> says: " + message);
                    message = "<"+this.sock.getLocalPort()+"> says: " + message;
                    new Sender(message).start();
                }

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}

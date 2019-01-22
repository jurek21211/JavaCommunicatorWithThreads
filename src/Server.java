import javax.print.attribute.standard.Severity;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<Socket> clientList = new ArrayList<Socket>();
    public Socket sock = null;

    public static void main(String args[]) throws IOException {
        ServerSocket serv = new ServerSocket(80);

        while (true) {
            System.out.println("Waiting for connection...");
            Socket sock = serv.accept();
            new TaskHandler(sock).start();
            clientList.add(sock);
        }
    }
}

class Sender extends Thread {
    String message = null;
    String exitOrder = null;
    Socket sock = null;

    PrintWriter output = null;

    Sender(String message, String exitOrder) {
        this.message = message;
        this.exitOrder = exitOrder;
    }

    public void run() {
        try {
                for(int i = 0; i < Server.clientList.size(); i++){
                output = new PrintWriter(Server.clientList.get(i).getOutputStream());
                output.println(this.message);
                output.flush();
            }
        } catch (IOException e) {

        }
    }
}


class TaskHandler extends Thread {
    Socket sock = null;
    BufferedReader reader = null;
    String message = null, reply = null;
    Server server = null;


    TaskHandler(Socket clientSocket) {
        try {
            this.sock = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {

            while (true) {
                message = this.reader.readLine();
                System.out.println("<Sender: " + this.sock.getPort() + "> says: " + message);
                reply = "<Client: " + this.sock.getPort() + "> says: " + message;
                new Sender(reply, message).start();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Connection with <" + this.sock.getPort() + "> terminated");
                    this.reader.close();
                    Server.clientList.remove(this.sock);
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


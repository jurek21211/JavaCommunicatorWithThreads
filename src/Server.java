import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<Socket> clientList = new ArrayList<Socket>();
    Socket sock = null;

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
    PrintWriter output = null;

    Sender(String message) {
        this.message = message;
    }

    public void run() {
        try {

            for (int i = 0; i < Server.clientList.size(); i++) {
                output = new PrintWriter(Server.clientList.get(i).getOutputStream());
                output.println(this.message);
                output.flush();
                if (message.equalsIgnoreCase("exit"))
                    output.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class TaskHandler extends Thread {
    Socket sock = null;
    BufferedReader reader = null;
    String message = null;
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
                message = "<Client: " + this.sock.getPort() + "> says: " + message;
                new Sender(message).start();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Connection terminated");
                    this.reader.close();
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static ArrayList<Socket> clientList = new ArrayList<Socket>();
    Socket sock = null;

    public static void main(String args[]) throws IOException {
        ServerSocket serv = new ServerSocket(80);

        while (true) {
            System.out.println("Waiting for connection...");
            Socket sock = serv.accept();
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
                output = new PrintWriter(clientList.get(i).getOutputStream());
                output.println(this.message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class TaskHandler extends Thread {
    Socket sock = null;
    BufferedReader reader;
    String message = null;
    Server server = null;


    TaskHandler(Socket clientSocket) {
        try {
            this.sock = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {

            while (true) {
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Connection terminated");
                    reader.close();
                    break;
                } else {
                    message = reader.readLine();
                    System.out.println("<" + this.sock.getLocalPort() + "> says: " + message);
                    message = "<" + this.sock.getLocalPort() + "> says: " + message;
                    new Sender(message).start();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


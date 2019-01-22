import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String args[]) throws IOException {

        Socket sock = null;
        BufferedReader keyboard = null;
        PrintWriter output = null;
        String message = "";

        sock = new Socket("127.0.0.1", 80);
        System.out.println("<Connected to: >" + sock);

        new Receiver(sock).start();

        while (true) {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintWriter(sock.getOutputStream());
            message = keyboard.readLine();
            output.println(message);
            output.flush();

            if (message.equalsIgnoreCase("exit")) {
                keyboard.close();
                output.close();
                sock.close();
                break;
            }

        }
    }

}

class Receiver extends Thread {
    Socket sock = null;
    BufferedReader sockReader = null;
    String message = "";

     Receiver(Socket sock) throws IOException {
        this.sock = sock;
        this.sockReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    public void run() {
        try {
            while (true) {

                if (message.equalsIgnoreCase("exit")) {
                    this.sockReader.close();
                    System.out.println("Connection terminanted");
                    break;
                }
                this.sockReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                message = this.sockReader.readLine();
                System.out.println("<Received message:> " + message);
            }

        } catch (IOException e) {
            System.out.println("Connection terminated");
        }
    }

}
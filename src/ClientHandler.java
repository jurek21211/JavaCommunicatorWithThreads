import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread {
    Socket socket = null;
    BufferedReader inputString = null, server = null;
    PrintWriter outputString;
    DateFormat fordate = new SimpleDateFormat("dd/MM/yyyy");

    ClientHandler(Socket clientSocket, BufferedReader inputString, PrintWriter outputString) {
        this.socket = clientSocket;
        this.inputString = inputString;
        this.outputString = outputString;
    }

    String received = null, toReturn = null;

    public void run() {
        String received = null, toReturn = null;
        while (true) {
            try {
                this.outputString.println("What do you want to do? ");

                if (received.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + this.socket + "sends exit...");
                    System.out.println("Disconnecting");
                    this.socket.close();
                    System.out.println("Disconnected");
                    break;

                }

                switch (received.toLowerCase()) {
                    case "date":
                        Date date = new Date();
                        toReturn = fordate.format(date);
                        outputString.println(toReturn);
                        break;
                    default:
                        outputString.println("Invalid Input");
                        break;
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }
        try {
            //closing connection
            this.inputString.close();
            this.outputString.close();
        }
        catch(IOException e ){
            System.out.println(e);
        }
    }
}


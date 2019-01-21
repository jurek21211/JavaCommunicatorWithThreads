import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private ConnectionToServer server = null;
    private LinkedBlockingQueue<Object> messages = null;
    private Socket socket = null;

    public Client(String IPAddress, int port) throws IOException {
        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<Object>();
        server = new ConnectionToServer(socket);

        Thread messageHandling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Object message = messages.take();
                        System.out.println("Message Received " + message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    private class ConnectionToServer {
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {
            this.socket = socket;
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            Thread read = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Object obj = input.readObject();
                            messages.put(obj);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }

        private void write(Object obj) {
            try {
                output.writeObject(obj);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void send(Object obj){
        server.write(obj);
    }
}

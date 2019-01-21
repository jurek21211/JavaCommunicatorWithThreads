import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    private ArrayList<ConnectionToClient> clientList = null;
    private LinkedBlockingQueue<Object> messages = null;
    private ServerSocket serverSocket = null;

    public Server(int port) throws IOException {
        clientList = new ArrayList<ConnectionToClient>();
        messages = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(port);

        Thread acceptConnection = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Socket s = serverSocket.accept();
                        clientList.add(new ConnectionToClient(s));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        acceptConnection.setDaemon(true);
        acceptConnection.start();

        Thread messageHandling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Object message = messages.take();
                        System.out.println("Message received " + message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        messageHandling.setDaemon(true);
        messageHandling.start();

    }

    private class ConnectionToClient {
        ObjectInputStream input;
        ObjectOutputStream output;
        Socket socket;

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());

            Thread read = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Object obj = input.readObject();
                            messages.put(obj);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }

        public void write(Object obj) {
            try{
                output.writeObject(obj);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void sendToAll(Object message){
            for(ConnectionToClient client : clientList)
                client.write(message);
        }
    }
}

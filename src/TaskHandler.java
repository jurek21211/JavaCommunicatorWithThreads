import java.net.Socket;

public class TaskHandler extends Thread {
    Socket sock = null;

    TaskHandler(Socket clientSocket){
        this.sock = clientSocket;
    }

    public void run(){

    }
}

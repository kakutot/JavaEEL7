package app.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    private SocketClient(){}

    private static class LazySingletonHolder {
        public static SocketClient INSTANCE = new SocketClient();
    }
    public static SocketClient getInstance() {
        return LazySingletonHolder.INSTANCE;
    }
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //handleMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Object msg) {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New delete message is sent to the server : " + msg);
        out.println(msg);

        out.close();
        return ;
    }

    private void handleMessages(){
        ((Runnable) () -> {
            while (true) {
                try {
                    String msg;
                    if ((msg = in.readLine()) != null) {
                        System.out.println("New message from server : " + msg);
                    }
                }
                catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Server died");
                    break;
                }
            }
        }).run();
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

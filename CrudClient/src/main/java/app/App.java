package app;

import app.socket.SocketChannelClient;
import app.socket.SocketUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        SocketChannelClient client = SocketChannelClient.getInstance();
        client.startConnection();

        //client.stopConnection();
    }


}
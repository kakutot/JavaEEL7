package application;

import application.socket.SocketChannelClient;
import application.socket.SocketUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        SocketChannelClient client = SocketChannelClient.getInstance();
        client.startConnection();

        //client.stopConnection();
    }

}
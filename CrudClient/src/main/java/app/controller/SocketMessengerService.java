package app.controller;

import app.socket.CommonModel;
import app.socket.Method;
import app.socket.SocketChannelClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SocketMessengerService {
    private ExecutorService executors;
    private SocketChannelClient socketClient;


    public SocketMessengerService(){
        socketClient = SocketChannelClient.getInstance();
        executors = Executors.newCachedThreadPool();
    }

    public void sendNewDeleteMessage(String id){
        executors.submit(
                ()-> socketClient.sendMessage(
                        prepareMsg(new CommonModel(parseId(id), Method.DELETE,System.currentTimeMillis()))));
    }
    public void sendNewInsertMessage(String id){
        executors.submit(
                ()-> socketClient.sendMessage(
                        prepareMsg(new CommonModel(parseId(id), Method.INSERT,System.currentTimeMillis()))));
    }
    public void sendNewUpdateMessage(String id){
        executors.submit(
                ()-> socketClient.sendMessage(
                        prepareMsg(new CommonModel(parseId(id), Method.UPDATE,System.currentTimeMillis()))));
    }

    private String prepareMsg(CommonModel message){
        ObjectMapper mapper = new ObjectMapper();
        String res = "";
        try {
            res = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return res;
    }
    private Integer parseId(String id){
        return Integer.valueOf(id);
    }
}

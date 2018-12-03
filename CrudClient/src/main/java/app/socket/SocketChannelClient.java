package app.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelClient {
    private SocketChannel socketChannel;
    private SocketChannelClient(){}

    private static class LazySingletonHolder {
        public static SocketChannelClient INSTANCE = new SocketChannelClient();

    }
    public static SocketChannelClient getInstance() {
        return SocketChannelClient.LazySingletonHolder.INSTANCE;
    }
    public void startConnection() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(SocketUtils.getHostNumber(),
                    Integer.valueOf(SocketUtils.getPortNumber())));
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessage(Object msg) {
        System.out.println("New delete message is sent to the server : " + msg.toString());
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        buffer.put(String.valueOf(msg.toString()).getBytes());
        buffer.flip();

        try {
            socketChannel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopConnection() {
        System.out.print("Channel connection is stopped");
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

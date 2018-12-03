package application.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketChannelClient {
    private static SocketChannel socketChannel;
    private static RecvThread messagesHandler;
    private static ConcurrentLinkedQueue<CommonModel> messagesToViewClient = new ConcurrentLinkedQueue<>();

    private SocketChannelClient(){}

    public static SocketChannelClient getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    public void startConnection() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(SocketUtils.getHostNumber(),
                    Integer.valueOf(SocketUtils.getPortNumber())));
            socketChannel.configureBlocking(false);

            handleMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleMessages(){
        messagesHandler = new RecvThread(socketChannel);
        messagesHandler.start();
    }

    public void stopConnection() {
        try {
            messagesHandler.val = false;
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class RecvThread extends Thread {

        public SocketChannel sc;
        public boolean val = true;

        public RecvThread(SocketChannel client) {
            sc = client;
        }

        public void run() {
            System.out.println("Inside receivemsg");
            ByteBuffer buf = ByteBuffer.allocate(2048);

            try {
                while (val) {
                    if ((sc.read(buf)) > 0){
                        buf.flip();
                        Charset charset = Charset.forName("UTF-8");
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer charBuffer = decoder.decode(buf);
                        CommonModel result = JsonUtils.parseFromJson(charBuffer.toString());

                        System.out.println("New message from server :" + result);
                        messagesToViewClient.offer(result);
                        buf.clear();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Server error");
            }
        }
    }

    public static CommonModel getCurrentMessage() {
        CommonModel res = messagesToViewClient.poll();
        return res;
    }

    private static class LazySingletonHolder {
        public static SocketChannelClient INSTANCE = new SocketChannelClient();
    }
}

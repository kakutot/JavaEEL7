package socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.CommonModel;

import java.io.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Server {
    ConcurrentLinkedQueue<CommonModel> messagesToViewClient = new ConcurrentLinkedQueue<>();
    private String latestMessage;
    private Selector selector;
    private SocketChannel socketChannel;
    private List<ServerSocketChannel> serverSocketChannels = new ArrayList<>();

    private Server (){
        try {
            selector = Selector.open();
            prepareSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class LazySingletonHolder {
        public static Server INSTANCE;

        static {
                INSTANCE = new Server();
        }
    }

    public static Server getInstance()
    {
        return LazySingletonHolder.INSTANCE;
    }


    public void start() throws IOException {
        System.out.println("Server started listening to provided ports " +
                ServerUtils.getSocketsInfo().values().stream()
                        .map(ServerUtils.ClientSocketInfo::getPortNumber)
                        .collect(Collectors.toList()));

        while (true) {
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    SocketAddress remoteAddr = socketChannel.socket().getRemoteSocketAddress();
                    System.out.println("Client is ready from address: " + socketChannel.socket()
                            .getLocalSocketAddress());
                    System.out.println("Connected to: " + remoteAddr);

                    /*
                     * Register channel with selector for further IO (record it for read/write
                     * operations, here we have used read operation)
                     */
                    int localPort = ((ServerSocketChannel) key.channel()).socket().getLocalPort();
                    if(ServerUtils.isCrudClient(localPort)){
                        socketChannel.register(this.selector, SelectionKey.OP_READ );
                        System.out.println("Client is registered for reading operation");
                    }
                    if(ServerUtils.isViewClient(localPort)){
                        socketChannel.register(this.selector, SelectionKey.OP_WRITE );
                        System.out.println("Client is registered for writing operation");
                    }
                }
                if(key.isReadable()){
                    if(key.isConnectable()){
                        key.channel().close();
                        break;
                    }

                    //System.out.println("Key is readable");
                    socketChannel = (SocketChannel) key.channel();
                    messagesToViewClient.add(readMessage(key));
                    System.out.println("Read from client with local address : " + ((SocketChannel) key.channel()).socket().getLocalSocketAddress()
                            + " : " + messagesToViewClient.peek().toString());
                }

                if (key.isWritable() && getFirstMessage() != null) {
                    if(key.isConnectable()){
                        key.channel().close();
                        break;
                    }
                   // System.out.println("Key is writable");
                    socketChannel = (SocketChannel) key.channel();
                    writeMessage(key, getFirstMessage());
                    removeOldestMsg();
                    }
                }
            }
    }

    public CommonModel getFirstMessage(){
        return messagesToViewClient.peek();
    }
    public void removeOldestMsg(){
        messagesToViewClient.poll();
    }
    private void prepareSelector() throws IOException {
        for(ServerUtils.ClientSocketInfo client : ServerUtils.getSocketsInfo().values()){
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(client.getPortNumber()));
            server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Binded new server channel on : " + server.getLocalAddress());
            serverSocketChannels.add(server);
        }
    }


    private CommonModel readMessage(SelectionKey key) {
        CommonModel commonModel = null;
        if(!key.channel().isOpen()){
            System.out.println("Channel" + key.channel() + "is closed -> reading can't be done");
        }else {
            SocketChannel sChannel = (SocketChannel) key.channel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            commonModel = new CommonModel();
            try {
                //bytes parsing
                sChannel.read(buf);
                buf.flip();
                Charset charset = Charset.forName("UTF-8");
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer = decoder.decode(buf);
                //json parsing
                commonModel = JsonUtils.parseFromJson(charBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return commonModel;
    }

    private void writeMessage (SelectionKey key,CommonModel commonModel){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(JsonUtils.parseToson(commonModel).getBytes());
        buffer.flip();

        Charset set = Charset.forName("UTF-8");
        CharsetDecoder dec = set.newDecoder();
        CharBuffer charBuf;
        try {
            charBuf = dec.decode(buffer);
            buffer = ByteBuffer.wrap((charBuf.toString()).getBytes());
            if(key.channel().isOpen()){
                ((SocketChannel)key.channel()).write(buffer);
            }else{
                System.out.println("Channel" + key.channel() + "is closed -> Writing can't be done");
            }
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        try {
            for(ServerSocketChannel server : serverSocketChannels){
                server.close();
            }
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

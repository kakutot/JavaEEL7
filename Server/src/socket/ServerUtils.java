package socket;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerUtils {
    private final static String HOST_NUMBER = "host";
    private final static String PORT_NUMBER = "port";
    private final static String CRUD_CLIENT_FILE_PATH = "C:\\Users\\roma.tupkalenko\\IdeaProjects\\l7\\scrudconfig.txt";
    private final static String VIEW_CLIENT_FILE_PATH = "C:\\Users\\roma.tupkalenko\\IdeaProjects\\l7\\sconfig.txt";
    private final static String FILE_DELIM = "-";
    private final static Map<Integer,ClientSocketInfo> socketsInfo;
    private static Map<String,String> socketInfoConsts;

    static {
        socketsInfo = new HashMap<>();
        socketInfoConsts = new HashMap<>();
        fillSocketConsts();
        initSocketsInfo();
    }

    public static boolean isCrudClient(int port){
        ClientSocketInfo client;
        boolean res = false;

        if((client = socketsInfo.get((port))) != null){
            res = client.clientType.equals(ClientType.CRUD) && (port == client.getPortNumber());
        }

        return res;
    }

    public static boolean isViewClient(int port){
        ClientSocketInfo client;
        boolean res = false;

        if((client = socketsInfo.get((port))) != null){
            res = client.clientType.equals(ClientType.VIEW) && (port == client.getPortNumber());
        }

        return res;
    }

    private static void fillSocketConsts() {
        socketInfoConsts = new HashMap<>();
        socketInfoConsts.put(HOST_NUMBER,HOST_NUMBER);
        socketInfoConsts.put(PORT_NUMBER,PORT_NUMBER);
    }

    private static void initSocketsInfo(){
        try {
            ClientSocketInfo crudClient = readFromFileSocketInfo(CRUD_CLIENT_FILE_PATH);
            socketsInfo.put(crudClient.getPortNumber(),crudClient);
            ClientSocketInfo viewClient = readFromFileSocketInfo(VIEW_CLIENT_FILE_PATH);
            socketsInfo.put(viewClient.getPortNumber(),viewClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ClientSocketInfo readFromFileSocketInfo(String filePath) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st;
        ClientSocketInfo clientSocketInfo = new ClientSocketInfo(filePath);

        while((st = br.readLine()) != null){
            String key = parseKey(st);
            if(socketInfoConsts.containsKey(key) && (key.equals(HOST_NUMBER))){
                clientSocketInfo.setHostNumber(parseValue(st,HOST_NUMBER));
            }
            if(socketInfoConsts.containsKey(key) && (key.equals(PORT_NUMBER))){
                clientSocketInfo.setPortNumber(Integer.valueOf(parseValue(st,PORT_NUMBER)));
            }
        }

        return clientSocketInfo;
    }

    private static String parseValue(String line,String pattern){
        String res = "";

        if(line.contains(pattern)){
            res =  line.substring(line.indexOf(pattern)+pattern.length()+1);
        }

        return res;
    }

    private static String parseKey(String line){
        return line.substring(0,line.indexOf(FILE_DELIM));
    }

    public static Map<Integer, ClientSocketInfo> getSocketsInfo() {
        return socketsInfo;
    }
    public static class ClientSocketInfo{
        private String hostNumber;
        private int portNumber;
        private ClientType clientType;

        public ClientSocketInfo(String filePath){
            if(CRUD_CLIENT_FILE_PATH.equals(filePath)){
                this.setClientType(ClientType.CRUD);
            }
            if(VIEW_CLIENT_FILE_PATH.equals(filePath)){
                this.setClientType(ClientType.VIEW);
            }
        }

        public ClientSocketInfo(String hostNumber, int portNumber, ClientType clientType) {
            this.hostNumber = hostNumber;
            this.portNumber = portNumber;
            this.clientType = clientType;
        }

        public String getHostNumber() {
            return hostNumber;
        }

        public void setHostNumber(String hostNumber) {
            this.hostNumber = hostNumber;
        }

        public int getPortNumber() {
            return portNumber;
        }

        public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
        }

        public ClientType getClientType() {
            return clientType;
        }

        public void setClientType(ClientType clientType) {
            this.clientType = clientType;
        }
    }
}

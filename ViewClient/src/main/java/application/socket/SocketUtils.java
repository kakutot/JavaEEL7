package application.socket;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import java.io.*;

public class SocketUtils {
    private final static String HOST_NUMBER = "host";
    private final static String PORT_NUMBER = "port";
    private final static String FILE_PATH = "C:\\Users\\roma.tupkalenko\\IdeaProjects\\l7\\sconfig.txt";
    private final static String FILE_DELIM = "-";
    private static Map<String,String> socketInfo;

    static {
        initAndGetSocketInfo();
    }
    private static Map initAndGetSocketInfo(){
        socketInfo = new HashMap<>();

        socketInfo.put(PORT_NUMBER,"");
        socketInfo.put(HOST_NUMBER,"");

        try {
           readFromFileToMap(socketInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socketInfo;
    }

    private static void readFromFileToMap(Map map) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(FILE_PATH)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st;
        while((st = br.readLine()) != null){
            String key = parseKey(st);
            if(map.containsKey(key)){
                map.put(key,parseValue(st,key));
            }
        }
    }

    private static String parseValue(String line,String pattern){
        String res = "";

        if(line.contains(pattern)){
            res =  line.substring(line.indexOf(pattern)+pattern.length()+1);
        }

        return res;
    }

    private static String parseKey(String line){
        return line.substring(0,line.indexOf('-'));
    }

    public static String getHostNumber() {
        return socketInfo.get(HOST_NUMBER);
    }

    public static String getPortNumber() {
        return socketInfo.get(PORT_NUMBER);
    }

    private static boolean isInfoMapEmty(){
        return socketInfo != null;
    }

}

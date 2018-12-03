package application.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    public static CommonModel parseFromJson(String message) {
        ObjectMapper mapper = new ObjectMapper();
        CommonModel commonModel = new CommonModel();
        try {
            commonModel = mapper.readValue(message, CommonModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commonModel;
    }

    public static String parseToson(CommonModel commonModel) {
        ObjectMapper mapper = new ObjectMapper();
        String res = "";
        try {
            res = mapper.writeValueAsString(commonModel);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return res;
    }
}

package application.websocket;
import java.util.Date;

public class FacultyRequest {

   private String requestMsg;

    public FacultyRequest(){};

    public FacultyRequest(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }
}

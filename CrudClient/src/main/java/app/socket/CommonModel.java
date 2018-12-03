package app.socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommonModel {
    private Integer rowId;
    private Method method;
    private long time;

    public CommonModel(){
    }

    public CommonModel(Integer rowId, Method method, long time) {
        this.rowId = rowId;
        this.method = method;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}

package model;

import java.text.SimpleDateFormat;
import java.util.*;
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

    @Override
    public String toString() {
        return String.format("CommonModel{" +
                "rowId=" + rowId +
                ", method=" + method +
                ", time=" + new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .format(new Date(time)) + '}');
    }
}

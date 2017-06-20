package com.hhyg.TyClosing.entities.shopcart;

import java.util.Map;

/**
 * Created by user on 2017/6/19.
 */

public class ShopcartListRes {

    /**
     * errcode : 1
     * op : padcartlist
     * data : {"active_columns":{}}
     * channel : 673
     */

    private int errcode;
    private String op;
    private DataBean data;
    private String channel;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "ShopcartListRes{" +
                "errcode=" + errcode +
                ", op='" + op + '\'' +
                ", data=" + data.toString() +
                ", channel='" + channel + '\'' +
                '}';
    }

    public static class DataBean {
        private Map<String,CastDetail> active_columns;

        public Map<String, CastDetail> getActive_columns() {
            return active_columns;
        }

        public void setActive_columns(Map<String, CastDetail> active_columns) {
            this.active_columns = active_columns;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "active_columns=" + active_columns.keySet().toString() +
                    '}';
        }
    }
}

package com.hhyg.TyClosing.entities.associate;

import java.util.List;

/**
 * Created by user on 2017/6/14.
 */

public class AssociateRes {

    /**
     * errcode : 1
     * op : emptyop
     * channel : 673
     * data : [{"name":"高培","count":3},{"name":"隔离防晒","count":86},{"name":"工具","count":38},{"name":"AGATHA","count":29},{"name":"古驰","count":239},{"name":"古驰男士","count":5},{"name":"锅具","count":6},{"name":"哥蒂梵","count":3},{"name":"盖尔斯","count":714},{"name":"光学眼镜","count":105}]
     * msg : 成功
     */

    private int errcode;
    private String op;
    private String channel;
    private String msg;
    private List<DataBean> data;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 高培
         * count : 3
         */

        private String name;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}

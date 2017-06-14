package com.hhyg.TyClosing.entities.associate;

/**
 * Created by user on 2017/6/14.
 */

public class AssociateParam {

    /**
     * op : associate
     * channel : 体验店渠道号
     * imei : imei号
     * shopid : 体验店ID
     * platformId : 平台号，Android传3，H5传1
     * data : {"keyword":"搜索关键字（非必需(需要UrlEncode 特殊字符需要进行处理)"}
     */

    private String op;
    private String channel;
    private String imei;
    private String shopid;
    private String platformId;
    private DataBean data;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * keyword : 搜索关键字（非必需(需要UrlEncode 特殊字符需要进行处理)
         */

        private String keyword;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }
}

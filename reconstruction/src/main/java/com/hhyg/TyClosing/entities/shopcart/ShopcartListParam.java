package com.hhyg.TyClosing.entities.shopcart;

import java.util.List;

/**
 * Created by user on 2017/6/19.
 */

public class ShopcartListParam {

    /**
     * deliverplace : 3
     * imei : d30a10153d976f3a
     * data : [{"number":1,"activity":"AI2017052613434738231","barcode":"3380810008593"},{"number":1,"activity":"AI2017052613434738231","barcode":"3380810008616"},{"number":1,"activity":"AI2017052613434738231","barcode":"3380810016413"},{"number":1,"activity":"AI2017052613434738231","barcode":"3380810052640"}]
     * shopid : 1
     * platformid : 3
     * saler_id : 297
     * channel : 673
     */

    private String deliverplace;
    private String imei;
    private String shopid;
    private String platformid;
    private String saler_id;
    private String channel;
    private String platformId;
    private List<DataBean> data;

    public String getDeliverplace() {
        return deliverplace;
    }

    public void setDeliverplace(String deliverplace) {
        this.deliverplace = deliverplace;
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

    public String getPlatformid() {
        return platformid;
    }

    public void setPlatformid(String platformid) {
        this.platformid = platformid;
    }

    public String getSaler_id() {
        return saler_id;
    }

    public void setSaler_id(String saler_id) {
        this.saler_id = saler_id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * number : 1
         * activity : AI2017052613434738231
         * barcode : 3380810008593
         */

        private String number;
        private String activity;
        private String barcode;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }
}

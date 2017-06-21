package com.hhyg.TyClosing.entities.shopcart;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2017/6/19.
 */

public class CastDetail {

    /**
     * total_price : 2134
     * short_desc : 满200减20, 满300减30, 满400减40, 满500减50, 满600减60, 满700减70, 满800减80, 满900减90, 满1000减100, 满1500减200
     * total_fee : 200
     * type_name : 满额减
     * type : 3
     * desc_fee : 已满足【满1500减200】
     * desc : 满200元减20.00元 满300元减30.00元 满400元减40.00元 满500元减50.00元
     * preferentialPrice : 200
     * activity : AI2017051209253042960
     * isprivilege : 0
     * name : 满200元减20元 满300元减30元 满400元减40元 满500元减50.00元
     * active_price : 1934
     */
    private String total_price;
    private String short_desc;
    private String total_fee;
    private String type_name;
    private String type;
    private String desc_fee;
    private String desc;
    private String preferentialPrice;
    private String activity;
    private String name;
    private String active_price;

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc_fee() {
        return desc_fee;
    }

    public void setDesc_fee(String desc_fee) {
        this.desc_fee = desc_fee;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(String preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive_price() {
        return active_price;
    }

    public void setActive_price(String active_price) {
        this.active_price = active_price;
    }
}

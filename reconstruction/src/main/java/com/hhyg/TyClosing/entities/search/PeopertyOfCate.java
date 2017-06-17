package com.hhyg.TyClosing.entities.search;

import java.util.ArrayList;

/**
 * Created by user on 2017/6/15.
 */

public class PeopertyOfCate {
    private String cateId;
    private ArrayList<PropertyListBean> dataSet;

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public ArrayList<PropertyListBean> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<PropertyListBean> dataSet) {
        this.dataSet = dataSet;
    }

}

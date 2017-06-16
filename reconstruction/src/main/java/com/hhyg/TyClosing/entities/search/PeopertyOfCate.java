package com.hhyg.TyClosing.entities.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/6/15.
 */

public class PeopertyOfCate {
    private String cateId;
    private ArrayList<PropertyListBean> dataSet;
    public static class PropertyListBean {
        /**
         * name : 商品类型
         * value : ["粉底液","粉底霜"]
         */

        private String name;
        private List<String> value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

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

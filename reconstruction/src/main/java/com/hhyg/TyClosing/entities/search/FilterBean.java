package com.hhyg.TyClosing.entities.search;

import java.util.ArrayList;

/**
 * Created by user on 2017/6/13.
 */

public class FilterBean {
    private FilterType type;
    private ArrayList<FilterItem> dataSet = new ArrayList<>();
    private String name;

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public ArrayList<FilterItem> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<FilterItem> dataSet) {
        this.dataSet = dataSet;
    }

    public void addItem(FilterItem item){
        dataSet.add(item);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FilterBean{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}

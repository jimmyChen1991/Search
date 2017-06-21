package com.hhyg.TyClosing.entities.search;

import java.util.ArrayList;

/**
 * Created by user on 2017/6/13.
 */

public class FilterBean {
    private FilterType type;
    private ArrayList<FilterItem> dataSet = new ArrayList<>();
    private String name;
    private boolean selected;
    private boolean showNow;
    private boolean vertacalShow;
    private String selectedName;
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

    public void addItem(int position,FilterItem item){
        dataSet.add(position,item);
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public boolean isShowNow() {
        return showNow;
    }

    public void setShowNow(boolean showNow) {
        this.showNow = showNow;
    }

    public boolean isVertacalShow() {
        return vertacalShow;
    }

    public void setVertacalShow(boolean vertacalShow) {
        this.vertacalShow = vertacalShow;
    }
}

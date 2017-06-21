package com.hhyg.TyClosing.entities.search;

/**
 * Created by user on 2017/6/13.
 */

public class FilterItem {
    private String name;
    private String id;
    private String maxPrice;
    private String minPrice;
    private boolean selected;
    private boolean isAllchoseFlag;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isAllchoseFlag() {
        return isAllchoseFlag;
    }

    public void setAllchoseFlag(boolean allchoseFlag) {
        isAllchoseFlag = allchoseFlag;
    }
}

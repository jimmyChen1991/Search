package com.hhyg.TyClosing.entities.search;

import java.util.List;

/**
 * Created by user on 2017/6/16.
 */

public class PropertyListBean {
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

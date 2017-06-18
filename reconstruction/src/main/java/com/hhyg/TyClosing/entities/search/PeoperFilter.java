package com.hhyg.TyClosing.entities.search;

import java.util.ArrayList;

/**
 * Created by chenqiyang on 17/6/19.
 */

public class PeoperFilter {
    private String name;
    private ArrayList<FilterItem> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FilterItem> getValues() {
        return values;
    }

    public void setValues(ArrayList<FilterItem> values) {
        this.values = values;
    }
}

package com.hhyg.TyClosing.entities.search;

/**
 * Created by chenqiyang on 17/6/19.
 */

public class SearchFilterParam {
    private String param;
    private String param2;
    private FilterType type;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}

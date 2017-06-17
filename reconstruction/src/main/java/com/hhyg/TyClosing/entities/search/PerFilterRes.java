package com.hhyg.TyClosing.entities.search;

/**
 * Created by user on 2017/6/16.
 */

public class PerFilterRes {
    private SearchFilterRes raw;
    private String cateId;

    public SearchFilterRes getRaw() {
        return raw;
    }

    public void setRaw(SearchFilterRes raw) {
        this.raw = raw;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }
}

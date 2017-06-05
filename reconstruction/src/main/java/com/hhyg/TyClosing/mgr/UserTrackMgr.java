package com.hhyg.TyClosing.mgr;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.hhyg.TyClosing.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mjf on 2016/11/28.
 */
public class UserTrackMgr {
    private static UserTrackMgr mInstance = null;
    private List<String> mKeyPath = new ArrayList<String>();
    private String mGoodsID;
    private String mCatalogueID;
    private Map mMap;

    public static synchronized UserTrackMgr getInstance() {
        if (mInstance == null)
            mInstance = new UserTrackMgr();
        return mInstance;
    }

    public void enter(Object c){}
    public void enter(String str){}
    public void clear(){}
    public void setGoodsID(String str){
        mGoodsID = str;
    }
    public String getGoodsID(){
        if(StringUtil.isEmpty(mGoodsID))
            return "";
        return mGoodsID;
    }

    public void setCatalogueID(String str){
        mCatalogueID = str;
    }
    public String getCatalogueID(){
        if(StringUtil.isEmpty(mCatalogueID))
            return "";
        return mCatalogueID;
    }

    public void onEvent(String strEvent,String goodsid){
        Map<String, String> map_ekv = new HashMap<String, String>();
        if(StringUtil.isEmpty(goodsid) == false)
            map_ekv.put("goodsid", goodsid);
        MobclickAgent.onEvent(null, strEvent, map_ekv);
    }
}

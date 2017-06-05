package com.hhyg.TyClosing.ui.callback;

/**
 * Created by mjf on 2016/11/15.
 */
public interface ICheckPayResultCallBack {
    public void payFailed();
    public void PaySuccess( final com.alibaba.fastjson.JSONObject jb);
}

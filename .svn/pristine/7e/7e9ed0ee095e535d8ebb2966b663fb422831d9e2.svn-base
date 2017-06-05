package com.hhyg.TyClosing.presenter;

import android.os.Message;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.view.IsPrivilegeUserView;

import java.io.IOException;

/**
 * Created by user on 2017/5/24.
 */

public class IsprivilegeUserPresenter extends BasePresenter<IsPrivilegeUserView>{
    private final String ISPRIVILEGE_URL = Constants.getIndexUrl() + "?r=privilege/isprivilege";
    private final IsPrivilegeUserProc mProc = new IsPrivilegeUserProc();
    private final IsPrivilegeUserExceptionHandler mExceptionHandler = new IsPrivilegeUserExceptionHandler();

    public void isPrivilegeUser(){
        mHttpRequester.post(ISPRIVILEGE_URL,makeIsPrivilegeParam(),new NetCallBackHandlerException(mExceptionHandler,mProc));
    }

    private String makeIsPrivilegeParam(){
        JSONObject param = new JSONObject();
        param.put("op", "privilege_isprivilege");
        param.put("imei", MyApplication.GetInstance().getAndroidId());
        param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
        param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        param.put("platformId",3);
        return param.toString();
    }

    class IsPrivilegeUserProc implements ProcMsgHelper{
        @Override
        public void ProcMsg(String msgBody) throws JSONException, IOException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mView != null){
                        mView.isPrivilegeUser();
                    }
                }
            });
        }
    }

    class IsPrivilegeUserExceptionHandler extends SimpleHandler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mView != null){
                if(msg.what == 4){
                    mView.isNotPrivilegeUser();
                }else{
                    mView.isPrivilegeExceptionView();
                }
            }
        }
    }



}

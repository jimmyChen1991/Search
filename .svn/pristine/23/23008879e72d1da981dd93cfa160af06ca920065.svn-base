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
import com.hhyg.TyClosing.view.BindPrivilegeView;

import java.io.IOException;

/**
 * Created by user on 2017/5/24.
 */

public class BindPrivilegePresenter extends BasePresenter<BindPrivilegeView> {

    private final String BIND_URL = Constants.getIndexUrl() + "?r=privilege/bound";
    private final BindPrivilegeExceptionHandler mExceptionHandler = new BindPrivilegeExceptionHandler();
    private final BindPrivilegeProc mProc = new BindPrivilegeProc();

    public void bindPrivilegeCode(String code){
        if(mView != null){
            mView.startProgress();
        }
        mHttpRequester.post(BIND_URL,makeBindParam(code),new NetCallBackHandlerException(mExceptionHandler,mProc));
    }

    private String makeBindParam(String code){
        JSONObject param = new JSONObject();
        param.put("op", "privilege_bound");
        param.put("imei", MyApplication.GetInstance().getAndroidId());
        param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
        param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        param.put("codes",code);
        param.put("platformId",3);
        return param.toString();
    }

    class BindPrivilegeExceptionHandler extends SimpleHandler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mView != null){
                mView.disProgress();
            }
            if(mView != null && msg.what == 4){
                mView.bindFailed((String) msg.obj);
            }
        }
    }

    class BindPrivilegeProc implements ProcMsgHelper{
        @Override
        public void ProcMsg(String msgBody) throws JSONException, IOException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mView != null){
                        mView.disProgress();
                        mView.bindSuccess();
                    }
                }
            });
        }
    }

}

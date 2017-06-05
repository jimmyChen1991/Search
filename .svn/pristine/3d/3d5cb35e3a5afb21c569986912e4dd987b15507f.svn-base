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
import com.hhyg.TyClosing.view.CancelBindPrivilegeView;

import java.io.IOException;

/**
 * Created by user on 2017/5/24.
 */

public class CancelBindPrivilegePresenter extends BasePresenter<CancelBindPrivilegeView>{

    private final String CANCELBIND_URL = Constants.getIndexUrl() + "?r=privilege/unbound";
    private final CancelBindPrivilegeExceptionHandler mException = new CancelBindPrivilegeExceptionHandler();
    private final CancelBingPrivilegeProc mProc = new CancelBingPrivilegeProc();

    public void cancelBindPrivilege(String code){
        mHttpRequester.post(CANCELBIND_URL,makeCancelParam(code),new NetCallBackHandlerException(mException,mProc));
    }

    private String makeCancelParam(String code){
        JSONObject param = new JSONObject();
        param.put("op", "privilege_unbound");
        param.put("imei", MyApplication.GetInstance().getAndroidId());
        param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
        param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        param.put("codes",code);
        param.put("platformId",3);
        return param.toString();
    }

    class CancelBindPrivilegeExceptionHandler extends SimpleHandler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mView != null){
                if(msg.what == 4){
                    mView.cancelBindFailed((String) msg.obj);
                }else{
                    mView.cancelBindFailed(null);
                }
            }
        }
    }

    class CancelBingPrivilegeProc implements ProcMsgHelper{
        @Override
        public void ProcMsg(String msgBody) throws JSONException, IOException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mView != null){
                        mView.cancelBindSuccess();
                    }
                }
            });
        }
    }
}

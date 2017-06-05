package com.hhyg.TyClosing.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetExceptionAlert;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.ui.callback.ICheckPayResultCallBack;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.ToastUtil;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mjf on 2016/11/15.
 */
public class CheckPayResultNetWork {
    public String mUrl = null;
    public String mOrderNumber = null;
    public INetWorkCallBack mCall = new checkmyOrder();
    public Timer timer = null;
    public ICheckPayResultCallBack mCallBack = null;
    private Context mContext;

    public CheckPayResultNetWork(Context context,ICheckPayResultCallBack callBack,String orderNumber,String url) {
        mCallBack = callBack;
        mOrderNumber = orderNumber;
        mUrl = url;
        mContext = context;
    }

    public void startCkeckOrder() {
        stopCheckOrder();
        timer = new Timer();
        timer.schedule(new MyTask(), 300, 2 * 1000);
    }

    public void stopCheckOrder() {
        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    class MyTask extends TimerTask {
        @Override public void run() {
            com.alibaba.fastjson.JSONObject mapData1 = new com.alibaba.fastjson.JSONObject();
            mapData1.put("orderSn", mOrderNumber);
            mapData1.put("op", "ownorderstatus");
            try {
                MyApplication.GetInstance().post(mUrl, JsonPostParamBuilder.makeParam(mapData1), mCall);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class checkmyOrder implements INetWorkCallBack {
        public void PostProcess(int msgId, String msg) {
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(mContext, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                jsonObject = new com.alibaba.fastjson.JSONObject();
                ToastUtil.getLongToastByString(mContext, "数据异常");
//                Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
                return;
            }
            String type = jsonObject.getString("errcode");
            if (type.equals("1")) {
                final com.alibaba.fastjson.JSONObject jb = jsonObject.getJSONObject("data");
                Logger.GetInstance().Debug(jb.toString());
                final String strtus = jb.getString("status");
                if (strtus.equals("0")) {
                    String time = jb.getString("time");
                    if (time.equals("0")) {
                        payFailed();
                    }
                } else {
                    PaySuccess(jsonObject);
                }
            } else {
                String str = jsonObject.getString("errmsg");
                if(StringUtil.isEmpty(str) == false) {
                    Logger.GetInstance().Error("ownorderstatus error :  " + str);
                }
                payFailed();
            }
        }
    }
    public void payFailed(){
        stopCheckOrder();
        if(mCallBack != null)
            mCallBack.payFailed();
        mCallBack = null;
    }

    public void PaySuccess( final JSONObject jb){
        stopCheckOrder();
        if(mCallBack != null)
            mCallBack.PaySuccess(jb);
        mCallBack = null;
    }
}


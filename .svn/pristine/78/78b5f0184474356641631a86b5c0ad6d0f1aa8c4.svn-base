package com.hhyg.TyClosing.global;


import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.ToastUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * 网络请求异常提示工具类，
 * Created by chenggang on 2016/9/7.
 */
public class NetExceptionAlert {
    private MyApplication mApp = MyApplication.GetInstance();
    private Context context;
    private Dialog dialog;

    /**
     * @param context 当前界面的Context
     * @param dialog  loading弹出框，如果没有，则设为null
     */
    public NetExceptionAlert(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    /**
     * @param msgType 消息类型
     * @param msg     消息
     * @param
     * @return true 代表网络问题，false 非网络问题
     */
    public boolean netExceptionProcess(int msgType, final String msg) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        if(msgType == mApp.MSG_TYPE_ERROR){
        	activity.runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.getLongToastByString(context, msg);
                }
            });
        	return true;
        }
        if(msg.length()>6){
        	if(msg.substring(0, 6).equals("<html>")){
        		activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.getLongToastByString(context, "网络尚未连接");
                    }
                });
            	return true;
        	}
        }
		if(msg.length()>20){
			final String Authorizatio =  msg.substring(0, 20);
			if(Authorizatio.contains("<!DOCTYPE html>")){
				activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.getLongToastByString(context, "网络尚未连接");
                    }
                });
            	return true;
			}
		}
        try{
        	com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(msg);
        }catch(Exception e){
        	activity.runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.getLongToastByString(context, "数据异常");
                }
            });
        	return true;
        }
        return false;
    }


    public JSONObject netResultCkeck(int msgType, final String msg) {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        if(msgType == mApp.MSG_TYPE_ERROR){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.getLongToastByString(context, msg);
                }
            });
        }

        if(StringUtil.isEmpty(msg)){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.getLongToastByString(context, "返回数据null");
                }
            });
        }

        if(msg.length() > 6){
            if(msg.substring(0, 6).equals("<html>")){
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.getLongToastByString(context, "网络尚未连接");
                    }
                });
                return null;
            }
        }

        if(msg.length()>20){
            final String Authorizatio =  msg.substring(0, 20);
            if(Authorizatio.contains("<!DOCTYPE html>")){
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.getLongToastByString(context, "网络尚未连接");
                    }
                });
                return null;
            }
        }

        jsonObject = JsonPostParamBuilder.parseJsonFromString(msg);
        if(jsonObject == null){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.getLongToastByString(context, "json 解析错误");
                }
            });
        }
        return jsonObject;
    }


    public boolean checkJSonResult(com.alibaba.fastjson.JSONObject jsonObject,String strDefaultMSg) {
        if(jsonObject == null){
            return false;
        }
        String str = jsonObject.getString("errcode");
        if ("1".equals(str) == false) {
            String strErrorMsg = jsonObject.getString("msg");
            if (StringUtil.isEmpty(strErrorMsg))
                strErrorMsg = strDefaultMSg;
                final String msg = strErrorMsg;
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.getLongToastByString(context, msg);
                }
            });
            return false;
        }
        return true ;
    }
}

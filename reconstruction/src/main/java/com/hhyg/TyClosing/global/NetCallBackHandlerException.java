package com.hhyg.TyClosing.global;

import java.io.IOException;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import android.os.Handler;
import android.os.Message;
import com.hhyg.TyClosing.log.Logger;


public class NetCallBackHandlerException implements INetWorkCallBack{
	private Handler mExceptionHanlder;
	private ProcMsgHelper mProcMsgHelper;
	public NetCallBackHandlerException(Handler hanlder,ProcMsgHelper proc) {
		super();
		mExceptionHanlder = hanlder;
		mProcMsgHelper = proc;
	}
	@Override
	public void PostProcess(int msgId, String msg) {
		if (msgId == 0) { // 0对应MSG_MSG_TYPE_VALUE
			if(handlerParamExcetipn(msg))
				return;
			try {
				mProcMsgHelper.ProcMsg(msg);
			} catch (JSONException e) {
                Logger.GetInstance().Exception(e.getMessage() + " msg is :" + msg);
				// TODO Auto-generated catch block
				e.printStackTrace();
				mExceptionHanlder.sendEmptyMessage(0);
			} catch(IOException e){
                Logger.GetInstance().Exception(e.getMessage() + "msg is :" + msg);
				mExceptionHanlder.sendEmptyMessage(1);
			}          	
		} else if (msgId == 1) {//1对应MSG_TYPE_ERROR
			Message handlerMsg = NetCallBackMessageFactory.newInstanceOfMessageBymsg(msg);
			mExceptionHanlder.sendMessage(handlerMsg);
		}
	}
	private static class NetCallBackMessageFactory{
		private static Message newInstanceOfMessageBymsg(String msg){
			Message handlerMsg = new Message();
			handlerMsg.what = 2;
			handlerMsg.obj = msg;
			return handlerMsg;
		}
	}
	private boolean handlerParamExcetipn(String msgBody){
		boolean handed = false;
		if(msgBody.length()>6){
			final String Authorizatio =  msgBody.substring(0, 6);
			if(Authorizatio.equals("<html>")){
				Message handlerMsg = NetCallBackMessageFactory.newInstanceOfMessageBymsg("网络尚未连接");
				mExceptionHanlder.sendMessage(handlerMsg);
				handed = true;
				return handed;
			}
		}
		if(msgBody.length()>20){
			final String Authorizatio =  msgBody.substring(0, 20);
			if(Authorizatio.contains("<!DOCTYPE html>")){
				Message handlerMsg = NetCallBackMessageFactory.newInstanceOfMessageBymsg("网络尚未连接");
				mExceptionHanlder.sendMessage(handlerMsg);
				handed = true;
				return handed;
			}
		}
        try {
				JSONObject jsonObj = JSONObject.parseObject(msgBody);
				if(jsonObj == null){
					mExceptionHanlder.sendEmptyMessage(0);
					handed = true;
				}else if(jsonObj.getString("errcode") == null){
					mExceptionHanlder.sendEmptyMessage(0);
					handed = true;
				}else if(!jsonObj.getString("errcode").equals("1")){
					final String errMsg = jsonObj.getString("msg");
					Message msg = mExceptionHanlder.obtainMessage();
					msg.obj = errMsg;
					msg.what = 4;
					mExceptionHanlder.sendMessage(msg);
					handed = true;
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
            Logger.GetInstance().Exception(e.getMessage() + " msg is : " + msgBody);
			e.printStackTrace();
			mExceptionHanlder.sendEmptyMessage(0);
		} 
        return handed;
	}
}

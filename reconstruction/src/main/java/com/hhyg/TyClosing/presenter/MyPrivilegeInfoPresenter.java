package com.hhyg.TyClosing.presenter;

import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.PrivilegeInfo;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.view.MyPrivilegeView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 2017/5/24.
 */

public class MyPrivilegeInfoPresenter extends BasePresenter<MyPrivilegeView>{
    private final String MYPRIVILEGE_URL = Constants.getIndexUrl() + "?r=privilege/own";
    private final MyPrivilegeInfoEXceptionHandler mExceptionHandler = new MyPrivilegeInfoEXceptionHandler();
    private final MyPrivilegeInfoProc mProc = new MyPrivilegeInfoProc();

    public void fetchMyPrivilegeInfo(){
        mHttpRequester.post(MYPRIVILEGE_URL,makeFetchMyPrivilegeInfoParam(),new NetCallBackHandlerException(mExceptionHandler,mProc));
    }

    private String makeFetchMyPrivilegeInfoParam(){
        JSONObject param = new JSONObject();
        param.put("op", "privilege_own");
        param.put("imei", MyApplication.GetInstance().getAndroidId());
        param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
        param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        param.put("platformId",3);
        return param.toString();
    }

    class MyPrivilegeInfoEXceptionHandler extends SimpleHandler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    class MyPrivilegeInfoProc implements ProcMsgHelper{
        @Override
        public void ProcMsg(String msgBody) throws JSONException, IOException {
            JSONObject Res_JOBJ = JSON.parseObject(msgBody);
            JSONArray dataArr = Res_JOBJ.getJSONArray("data");
            PrivilegeInfo info = null;
            if(dataArr != null && dataArr.size() > 0){
                JSONObject privilegeJobj = dataArr.getJSONObject(0);
                JSONArray code_Jobj = privilegeJobj.getJSONArray("codes");
                if(code_Jobj != null && code_Jobj.size() > 0){
                    JSONObject code_item = code_Jobj.getJSONObject(0);
                    info = new PrivilegeInfo();
                    info.setDetailDesc(code_item.getString("description"));
                    info.setDiscountDesc(code_item.getString("discountDesc"));
                    if(!TextUtils.isEmpty(code_item.getString("startTime"))){
                        info.setStartTime(code_item.getString("startTime"));
                    }else{
                        info.setStartTime("");
                    }
                    if(!TextUtils.isEmpty(code_item.getString("endTime"))){
                        info.setEndTime(code_item.getString("endTime"));
                    }else{
                        info.setEndTime("");
                    }
                    info.setTitle(code_item.getString("codeName"));
                    info.setCode(code_item.getString("code"));
                    JSONArray activitys_Jobj = code_item.getJSONArray("activityCodes");
                    if(activitys_Jobj != null && activitys_Jobj.size() >0){
                        ArrayList<String> activitys = new ArrayList<>();
                        for(int idx = 0 ;idx < activitys_Jobj.size() ; idx ++){
                            activitys.add(activitys_Jobj.getString(idx));
                        }
                        info.setActivityCodes(activitys);
                    }
                }
            }
            final PrivilegeInfo pInfo  = info;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mView == null){
                        return;
                    }
                    if(pInfo != null){
                        mView.showMyPrivilegeInfo(pInfo);
                    }
                }
            });
        }
    }

    private String TimeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

}

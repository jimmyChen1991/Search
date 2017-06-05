package com.hhyg.TyClosing.mgr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONException;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.dao.ShoppingcartDao;
import com.hhyg.TyClosing.global.FileOperator;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.ui.CheckPayResultNetWork;
import com.hhyg.TyClosing.ui.GoodsInfoActivity;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.Validate;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Handler;
import java.util.zip.GZIPOutputStream;

import static android.content.Context.MODE_PRIVATE;
import static com.hhyg.TyClosing.config.Constants.IS_LOG_OPEN;

/**
 * Created by mjf on 2016/12/15.
 */
public class ServerLogMgr {
    private Timer timer;
    private static ServerLogMgr mInstance;
    private final int nInterval = 10 * 60 * 1000;
    private Context mContext = MyApplication.GetInstance().getApplicationContext();
    public final String NET_URL = Constants.getIndexUrl() + "?r=log/upload";
    private Thread MThread;

    public static ServerLogMgr getInstance() {
        if(mInstance == null) {
            mInstance = new ServerLogMgr();
        }
        return mInstance;
    }

    public ServerLogMgr(){
        if(IS_LOG_OPEN == false)
            return;
        if(timer != null)
            return;
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override public void run() {
                updateLog();
            }
        }, 0, nInterval);
    }

    public void setTag(String strPath){
        if(StringUtil.isEmpty(strPath) == true)
            return;
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor eidt = pref.edit();
        eidt.putString("fileDate", strPath);
        eidt.commit();
    }

    public String getTag(){
        SharedPreferences pref = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
        return pref.getString("fileDate", "");
    }

    private void updateLog() {
        class NetworkThread extends Thread {
            @Override public void run() {
                ArrayList<String> filelist = FileOperator.getAllFile(Logger.GetInstance().logFilePath);
                for (int i = filelist.size() - 1; i >= 0 ; i--) {
                    String strPath = filelist.get(i);
                    boolean nbDelete = true ;
                    do {
                        if (strPath.contains(Logger.GetInstance().BLANK_SPACE) == false)//文件属于旧格式，不上传
                            break;
                        if (strPath.contains("up") == false)//不是需要上传的日志，忽略
                            break;
                        File file = new File(strPath);
                        if(file.length() > Logger.GetInstance().maxFileSize * 4)//错误保护机制，用于防止上传超过限制的文件，避免程序崩溃
                            break;
                        nbDelete = false;
                    }
                    while (false);
                    if(nbDelete == true){
                        filelist.remove(i);
                    }
                }

                if(filelist.size() == 0)
                    return;

                int nStart = filelist.indexOf(getTag());
                if (nStart == -1)
                    nStart = 0;
                else//从下一个文件开始发送
                    nStart += 1;

                for (int i = nStart; i < filelist.size() - 1; i++) {//最新的一个文件不上传
                    String strPath = filelist.get(i);
                    String strContent = Logger.GetInstance().readFileContent(strPath);
                    if (StringUtil.isEmpty(strContent) == false)
                        sendDataToNet(strPath, strContent);
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                MThread = null;
            }
        }
        if (MyApplication.GetInstance().IsConnectingToInternet() && MThread == null) {
            MThread = new NetworkThread();
            MThread.start();
        }
    }

    private void sendDataToNet(final String strDate,String strContent) {
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();
        j.put("op", "uploadlog");
        j.put("data", strContent);
        MyApplication.GetInstance().post(  NET_URL, JsonPostParamBuilder.makeParam(j), new INetWorkCallBack() {
            public void PostProcess(int msgId, String msg) {
                com.alibaba.fastjson.JSONObject jsonObject = JsonPostParamBuilder.parseJsonFromString(msg);
                if (Validate.isObEqual("1",jsonObject.getString("errcode")))
                    setTag(strDate);
            };
        });
    }
}

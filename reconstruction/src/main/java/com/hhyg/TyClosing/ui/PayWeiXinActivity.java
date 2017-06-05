package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hhyg.TyClosing.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.ui.callback.ICheckPayResultCallBack;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.hhyg.TyClosing.global.*;


/**
 * Created by mjf on 16/8/17.
 */
public class PayWeiXinActivity extends Activity implements ICheckPayResultCallBack {
    private Handler handler = new Handler();
    private CheckPayResultNetWork mCheckPay = null;
    public INetWorkCallBack mCall = new call();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payweixin);

        ImageButton scanBt = (ImageButton) findViewById(R.id.button_scan1);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String str1 = getIntent().getStringExtra("weixinurl");
        String str2 = getIntent().getStringExtra("orderSn");
        String Str3 = getIntent().getStringExtra("finalPrice");

        try {
            MyApplication.GetInstance().post(str1, JsonPostParamBuilder.makeParam(), mCall);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage()+ " send data is :" + JsonPostParamBuilder.makeParam());
            e.printStackTrace();
        }

        TextView t = (TextView) findViewById(R.id.orderinfo);
        String str = getResources().getString(R.string.pay_result_order_number) + "  " + str2;
        t.setText(str);

        t = (TextView) findViewById(R.id.ordermoeny);
        str = getResources().getString(R.string.pay_result_mpney) + "  " + getResources().getString(R.string.server_settle_moeny) + Str3;
        t.setText(str);

        String orderNumber = getIntent().getStringExtra("orderSn");
        mCheckPay = new CheckPayResultNetWork(this,this, orderNumber, Constants.getServiceUrl());
        mCheckPay.startCkeckOrder();
        Logger.GetInstance().Track("PayWeiXinActivity on Create");
    }

    private class call implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(PayWeiXinActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage()+ "parse data is :" + msg);
                Toast.makeText(PayWeiXinActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                return;
            }
            com.alibaba.fastjson.JSONObject data = jsonObject.getJSONObject("msg");
            if("ok".equals(jsonObject.getString("ret"))){
                final String str = data.getString("wx_url");
                handler.post(new Runnable() {
                    public void run() {
                        if (StringUtil.isEmpty(str)) {
                            Toast.makeText(getApplicationContext(), "支付图片地址获取失败", Toast.LENGTH_SHORT).show();
                        } else {
                            final ImageView image = (ImageView) findViewById(R.id.buttoncode);
                            ImageLoader.getInstance().displayImage(str, image);
                        }
                    }
                });
            }
            else{
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "图片地址获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("PayWeiXinActivity");
        Logger.GetInstance().Track("PayWeiXinActivity on onResume");
    }

    @Override
    public void PaySuccess(final com.alibaba.fastjson.JSONObject jb) {
        String orderNumber = getIntent().getStringExtra("orderSn");
        String finalPrice = getIntent().getStringExtra("finalPrice");
        final String strWhereget = getIntent().getStringExtra("whereget");
        Intent intent = new Intent();
        intent.putExtra("orderSn", orderNumber);
        intent.putExtra("finalPrice", finalPrice);
        intent.putExtra("whereget", strWhereget);
        intent.putExtra("citOrderSn", getIntent().getStringExtra("citOrderSn"));
        intent.setClass(PayWeiXinActivity.this, PaySuccessActivity.class);
        startActivity(intent);
    }

    public void payFailed() {
        Intent intent = new Intent();
        intent.setClass(PayWeiXinActivity.this, AllShopActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("PayWeiXinActivity");
        Logger.GetInstance().Track("PayWeiXinActivity on onPause");
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mCheckPay.stopCheckOrder();
    }
}
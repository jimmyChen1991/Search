package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ImageButton;
import com.hhyg.TyClosing.R;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.ui.callback.ICheckPayResultCallBack;
import com.umeng.analytics.MobclickAgent;
import org.w3c.dom.Text;

/**
 * Created by mjf on 16/8/17.
 */
public class PayZhiFubaoActivity extends Activity implements ICheckPayResultCallBack {
    private WebView webView;
    private Handler handler = new Handler();
    private CheckPayResultNetWork mCheckPay = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payzhifubao);
        webView = (WebView) findViewById(R.id.webView1);

        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36" + " |touchScreen|");
        ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        final String strSuccessPayUrl = "http://pay.mianshui365.net/pay";//getIntent().getStringExtra("successPayUrl");

        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(strSuccessPayUrl) == false)
                    view.loadUrl(url);
                Log.d("112", "shouldOverrideUrlLoading: " + url);
                return false;
            }

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (consoleMessage.message().contains("Uncaught ReferenceError")) {

                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        String str = getIntent().getStringExtra("zhifubaourl");
        webView.loadUrl(str);

        ImageButton scanBt = (ImageButton) findViewById(R.id.button_scan1);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String orderNumber = getIntent().getStringExtra("orderSn");
        mCheckPay = new CheckPayResultNetWork(this,this, orderNumber, Constants.getServiceUrl());
        mCheckPay.startCkeckOrder();

        Logger.GetInstance().Track("PayZhiFubaoActivity on Create");
    }

    public void PaySuccess(final com.alibaba.fastjson.JSONObject jb) {
        String orderNumber = getIntent().getStringExtra("orderSn");
        String finalPrice = getIntent().getStringExtra("finalPrice");
        final String strWhereget = getIntent().getStringExtra("whereget");
        Intent intent = new Intent();
        intent.putExtra("orderSn", orderNumber);
        intent.putExtra("finalPrice", finalPrice);
        intent.putExtra("whereget", strWhereget);
        intent.putExtra("citOrderSn", getIntent().getStringExtra("citOrderSn"));
        intent.setClass(PayZhiFubaoActivity.this, PaySuccessActivity.class);
        startActivity(intent);
    }

    public void payFailed() {
        Intent intent = new Intent();
        intent.setClass(PayZhiFubaoActivity.this, AllShopActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("PayZhiFubaoActivity");
        Logger.GetInstance().Track("PayZhiFubaoActivity on onResume");
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("PayZhiFubaoActivity");
        Logger.GetInstance().Track("PayZhiFubaoActivity on onPause");
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mCheckPay.stopCheckOrder();
    }
}
package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.log.Logger;

/**
 * Created by mjf on 2016/11/23.
 */
public class BuyConditionActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buycondition);

        WebView webView = (WebView) findViewById(R.id.webView1);
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36" + " |touchScreen|");
        ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(Constants.V4BUYCONDITIONURL);

        ImageButton btn = (ImageButton) findViewById(R.id.button_scan1);
        btn.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BuyConditionActivity.this, SalerMainPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Logger.GetInstance().Track("BuyConditionActivity on Create");
    }
}

package com.hhyg.TyClosing.ui;

import android.app.Activity;
import com.hhyg.TyClosing.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.hhyg.TyClosing.log.Logger;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by mjf on 16/8/23.
 */
public class PayFailActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.payfail);

        ImageButton btn = (ImageButton) findViewById(R.id.button_scan1);
        btn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        Intent i = getIntent();
        String str = i.getStringExtra("orderSn");
        TextView t = (TextView)findViewById(R.id.order_info);
        if(t != null){
            String s = getResources().getString(R.string.pay_result_order_number) + " " + str;
            t.setText(s);
        }

        Button btn1 = (Button) findViewById(R.id.button_seeorder);
        btn1.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btn1 = (Button) findViewById(R.id.button_goon);
        btn1.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PayFailActivity.this, AllShopActivity.class);
                startActivity(intent);
            }
        });

        Logger.GetInstance().Track("PayFailActivity on Create");
    }
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public void back(){
        Intent intent = new Intent();
        intent.setClass(PayFailActivity.this, HistoryOrderActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("PayFailActivity");
        Logger.GetInstance().Track("PayFailActivity on onResume");
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("PayFailActivity");
        Logger.GetInstance().Track("PayFailActivity on onPause");
    }
}
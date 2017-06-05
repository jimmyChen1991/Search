package com.hhyg.TyClosing.ui;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mjf on 16/8/18.
 */
public class PaySuccessActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paysuccess);

        Button btn = (Button) findViewById(R.id.button_goon);
        btn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PaySuccessActivity.this, AllShopActivity.class);
                startActivity(intent);
            }
        });
        btn = (Button) findViewById(R.id.button_seeorder);
        btn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        ImageButton btn1 = (ImageButton) findViewById(R.id.button_scan1);
        btn1.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                back();
            }
        });

        String strWhereToGet =  getIntent().getStringExtra("whereget");
        ImageView vi = (ImageView) findViewById(R.id.button_pos);
        int rId =  R.drawable.map_haikou_in;
        if(ClosingRefInfoMgr.getInstance().isShopTypeOutside())
            rId =  R.drawable.map_haikou_out;
        if(strWhereToGet != null && strWhereToGet.contains("三亚")) {
            rId = R.drawable.map_sanya;
            TextView t = (TextView)findViewById(R.id.textview_prompt);
            t.setText("请于30分钟后,凭身份证及登机牌到前台提货");
        }
        vi.setImageResource(rId);

        if(ClosingRefInfoMgr.getInstance().isShopTypeOutside() && strWhereToGet != null){
            String strText = strWhereToGet.contains("三亚") ? "航班当天，请提前1小时，凭身份证及登机牌，前往三亚凤凰机场提货处提货"
                        : "航班当天，请提前1小时，凭身份证及登机牌，前往海口美兰机场提货处提货";
            ((TextView) findViewById(R.id.textview_prompt)).setText(strText);
        }

        String citOrderSn = getIntent().getStringExtra("citOrderSn");
        String str = getIntent().getStringExtra("orderSn");
        String strM = getIntent().getStringExtra("finalPrice");
        TextView  t = (TextView)findViewById(R.id.order_info);
        TextView  shangyou = (TextView) findViewById(R.id.order_info_shangyou);
        String shangyou_xiaoshou = "商友订单号 :"+citOrderSn +"    销售员:  "+ ClosingRefInfoMgr.getInstance().getSalerName() +"  "+ ClosingRefInfoMgr.getInstance().getUserName();
        shangyou.setText(shangyou_xiaoshou);
        if(t != null){
            String s = getResources().getString(R.string.pay_result_order_number) + str + "   "+ getResources().getString(R.string.pay_result_mpney) + " " +
                    getResources().getString(R.string.server_settle_moeny) + strM;
            t.setText(s);
        }

        Logger.GetInstance().Track("PaySuccessActivity on Create");
    }

    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public void back(){
        Intent intent = new Intent();
        intent.setClass(PaySuccessActivity.this, HistoryOrderActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("PaySuccessActivity");
        Logger.GetInstance().Track("PaySuccessActivity on onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("PaySuccessActivity");
        Logger.GetInstance().Track("PaySuccessActivity on onPause");
    }
}
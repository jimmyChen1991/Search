package com.hhyg.TyClosing.ui;

import com.hhyg.TyClosing.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.hhyg.TyClosing.log.Logger;
import com.umeng.analytics.MobclickAgent;

public class SuccessActivity extends Activity {
	private final Object lock1 = new Object();
	private String mOrderId = "";
	private String mCustomCode;
	private TextView mDingDanBianHao ;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);			
		Bundle b = getIntent().getExtras();
		mOrderId = b.getString("orderId");
		mCustomCode = b.getString("customcode");
		findView();
		ImageButton scanBt = (ImageButton) findViewById(R.id.button_scan1);
		scanBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Logger.GetInstance().Track("SuccessActivity on Create");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			synchronized (lock1) {
				jumpToShopcatActivity();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void findView() {
		TextView title = (TextView) findViewById(R.id.order);
		mDingDanBianHao = (TextView) findViewById(R.id.dingdanbianhao);
		if(mCustomCode!= null){
			title.setText("顾客码");
			mDingDanBianHao.setText(mCustomCode);
		}else{
			mDingDanBianHao.setText(mOrderId);
		}
		Button btn = (Button)findViewById(R.id.myorder);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(SuccessActivity.this, AllShopActivity.class);
				startActivity(it);
				finish();
			}
		});

		btn = (Button)findViewById(R.id.seeorder);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				jumpToShopcatActivity();
			}
		});
	}
	
	private void jumpToShopcatActivity(){
		Intent it = new Intent();
		it.setClass(SuccessActivity.this, HistoryOrderActivity.class);
		startActivity(it);
		finish();
	}
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SuccessActivity");
		Logger.GetInstance().Track("SuccessActivity on onResume");
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SuccessActivity");
		Logger.GetInstance().Track("SuccessActivity on onResume");
	}
}

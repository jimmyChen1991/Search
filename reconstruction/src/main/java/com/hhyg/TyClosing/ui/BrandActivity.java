package com.hhyg.TyClosing.ui;


import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.ui.view.AllShopTopEdit;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.ui.view.TopEdit;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BrandActivity extends Activity implements OnClickListener{
	private ImageButton scanBtn;
	private ProgressBar mProgressBar;
	private TopEdit topEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brand);
		findView();
		topEdit = new AllShopTopEdit(this);
		topEdit.topEdit();
		mProgressBar.startProgress();
		Logger.GetInstance().Track("BrandActivity on Create");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("BrandActivity");
        Logger.GetInstance().Track("BrandActivity on onResume");
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("BrandActivity");
        Logger.GetInstance().Track("BrandActivity on onPause");
	}
	
	private void findView() {
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		scanBtn = (ImageButton) findViewById(R.id.scan);
		scanBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		it.setClass(this, CaptureActivity.class);
		startActivity(it);
	}
	public void disBrandProgress(){
		mProgressBar.stopProgress();
	}
}

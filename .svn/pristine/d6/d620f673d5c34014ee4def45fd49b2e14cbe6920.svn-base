package com.hhyg.TyClosing.ui;
import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.DeleteMgr;
import com.hhyg.TyClosing.ui.adapter.DeleteAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class DeleteActivity extends Activity {
	MyApplication mApp = MyApplication.GetInstance();
	private DeleteMgr mDeleteMgr = DeleteMgr.getInstance();
	private ArrayList<ShoppingCartInfo> mInfoList;
    private RelativeLayout mNullLayOut = null;
	private ListView mLv = null;
	private DeleteAdapter mAdapter;
	private UIHandler mUiHandler = new UIHandler();
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete);
		Logger.GetInstance().Debug("come in");
		mInfoList = mDeleteMgr.getAll();
		findView();
		int cnt = mInfoList.size();
		if(cnt ==0) {
			mNullLayOut.setVisibility(View.VISIBLE);
			mLv.setVisibility(View.GONE);
		} else {
			mNullLayOut.setVisibility(View.GONE);
			mLv.setVisibility(View.VISIBLE);
		}
		Logger.GetInstance().Track("DeleteActivity on Create");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("DeleteActivity");
		Logger.GetInstance().Track("DeleteActivity on onResume");
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("DeleteActivity");
		Logger.GetInstance().Track("DeleteActivity on onPause");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

	private void findView() {
		mNullLayOut = (RelativeLayout) findViewById(R.id.nulllayout);
		ImageButton shoppingcart = (ImageButton) findViewById(R.id.button_shoppingcart);	
		shoppingcart.setOnClickListener(mShoppingCartClickLister);
		mLv = (ListView) findViewById(R.id.delete_lv);		
		mAdapter = new DeleteAdapter(this, mInfoList,mUiHandler) ;
		mLv.setAdapter(mAdapter);		
		ImageButton buttonClear = (ImageButton) findViewById(R.id.button_clear);	
		buttonClear.setOnClickListener(mClearClickLister);

	}
	
	
	private View.OnClickListener mShoppingCartClickLister = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(DeleteActivity.this, ShopCartActivity.class);
			startActivity(intent);
			DeleteActivity.this.finish();
		}
	};
	
	private View.OnClickListener mClearClickLister = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDeleteMgr.clear();
			mInfoList.clear();
			mAdapter.notifyDataSetChanged();
			mNullLayOut.setVisibility(View.VISIBLE);
			mLv.setVisibility(View.GONE);
		}
	};
	
	private class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 2: {
					mNullLayOut.setVisibility(View.VISIBLE);
					mLv.setVisibility(View.GONE);
					break;
				}
		 }
		}
	}
}

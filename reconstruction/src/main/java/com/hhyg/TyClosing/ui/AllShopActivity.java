package com.hhyg.TyClosing.ui;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.fragment.AllShopBaseFragment;
import com.hhyg.TyClosing.fragment.AllShopBrandFragment;
import com.hhyg.TyClosing.fragment.AllShopCateFragment;
import com.hhyg.TyClosing.fragment.AllShopFlashSaleFragment;
import com.hhyg.TyClosing.fragment.AllShopGiftFragment;
import com.hhyg.TyClosing.fragment.AllShopRecommendFragment;
import com.hhyg.TyClosing.fragment.AllShopSpecialFragment;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.presenter.AllshopPresenter;
import com.hhyg.TyClosing.presenter.IsprivilegeUserPresenter;
import com.hhyg.TyClosing.ui.view.AllShopTopEdit;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.ui.view.TopEdit;
import com.hhyg.TyClosing.view.AllShopView;
import com.hhyg.TyClosing.view.IsPrivilegeUserView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
public class AllShopActivity extends Activity implements OnClickListener,IsPrivilegeUserView{
	private ArrayList<AllShopBaseFragment> mFragList;
	private AllshopPresenter mAllshopPresenter;
	private IsprivilegeUserPresenter mIsprivilegeUserPresenter;
	private ImageButton brandBtn;
	private ImageButton cateBtn;
	private ImageButton homeBtn;
	private ImageButton scanBtn;
	private ImageButton shopcatBtn;
	private ImageButton hndfBtn;
	private TextView shopName;
	private TextView salerName;
	private PullToRefreshScrollView mPullToRefreshScrollView;
	private ProgressBar mProgressBar;
	private TopEdit topEdit;
	private ViewGroup mTop;
	private ImageView privilege_icon;
	private TextView privilege_tv;
	private boolean postable = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allshop);
		mFragList = new ArrayList<>();
		mAllshopPresenter = new AllshopPresenter();
		topEdit = new AllShopTopEdit(this);
		topEdit.setBackListener(this);
		topEdit.topEdit();
		findFragment();
		findView();
		mIsprivilegeUserPresenter = new IsprivilegeUserPresenter();
		mIsprivilegeUserPresenter.attach(this);
		Logger.GetInstance().Debug("AllShopActivity onCreate");
	}

	private void fetchLastedAllshopDataOnRefresh(){
		mAllshopPresenter.attach(new RestartView());
		mAllshopPresenter.fetchLastestAllShopData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mIsprivilegeUserPresenter.isPrivilegeUser();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0); //强制隐藏键盘
		fetchLastedAllshopDataOnRefresh();
		Logger.GetInstance().Track("AllShopActivity on onResume");
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		Logger.GetInstance().Track("AllShopActivity on onRestart");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		stopSpecialFragAutoMsgSend();
		super.onPause();
		Logger.GetInstance().Track("AllShopActivity on OnPause");
	}
	@Override
	protected void onDestroy() {
		mAllshopPresenter.detach();
		mIsprivilegeUserPresenter.detach();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			unregistSaler();
        }
		return super.onKeyDown(keyCode, event);
	}

	private void stopSpecialFragAutoMsgSend(){
		((AllShopSpecialFragment) mFragList.get(0)).stopAutoMsgSend();
	}

	private void findView(){
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.allshopsrcoll);
		mPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(postable){
					postable = false;
					stopSpecialFragAutoMsgSend();
					fetchLastedAllshopDataOnRefresh();
				}
				refreshView.onRefreshComplete();
			}
		});
		mPullToRefreshScrollView.setMode(Mode.PULL_FROM_START);
		brandBtn = (ImageButton) findViewById(R.id.brand);
		brandBtn.setOnClickListener(this);
		cateBtn = (ImageButton)findViewById(R.id.cate);
		cateBtn.setOnClickListener(this);
		homeBtn = (ImageButton) findViewById(R.id.home);
		homeBtn.setBackgroundResource(R.drawable.allshop_home_pressed);
		scanBtn = (ImageButton) findViewById(R.id.scan);
		scanBtn.setOnClickListener(this);
		shopcatBtn = (ImageButton) findViewById(R.id.shopcat);
		shopcatBtn.setOnClickListener(this);
		shopName = (TextView) findViewById(R.id.shopname);
		salerName = (TextView) findViewById(R.id.salername);
		shopName.setText(ClosingRefInfoMgr.getInstance().getSalerInfo().getShopName());
		salerName.setText("销售 : "+ClosingRefInfoMgr.getInstance().getSalerName() + ClosingRefInfoMgr.getInstance().getUserName());
		mTop = (ViewGroup) findViewById(R.id.top);
		privilege_icon = (ImageView) findViewById(R.id.allshop_privilege_icon);
		privilege_tv = (TextView) findViewById(R.id.allshop_privilege_desc);
		hndfBtn = (ImageButton) findViewById(R.id.backbtn);
		findViewById(R.id.go_privilege_wrap).setOnClickListener(this);
	}

	private void findFragment(){
		AllShopSpecialFragment specialFragment = (AllShopSpecialFragment) getFragmentManager().findFragmentById(R.id.special_frag);
		AllShopCateFragment cateFragment = (AllShopCateFragment)getFragmentManager().findFragmentById(R.id.cate_frag);
		AllShopBrandFragment brandFragment = (AllShopBrandFragment)getFragmentManager().findFragmentById(R.id.brand_frag);
		AllShopGiftFragment giftFragment = (AllShopGiftFragment)getFragmentManager().findFragmentById(R.id.gift_frag);
		AllShopRecommendFragment recommendFragment = (AllShopRecommendFragment)getFragmentManager().findFragmentById(R.id.recommend_frag);
		AllShopFlashSaleFragment flashSaleFragment = (AllShopFlashSaleFragment)getFragmentManager().findFragmentById(R.id.flash_frag);
		mFragList.add(specialFragment);
		mFragList.add(cateFragment);
		mFragList.add(brandFragment);
		mFragList.add(giftFragment);
		mFragList.add(recommendFragment);
		mFragList.add(flashSaleFragment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.brand:
				UserTrackMgr.getInstance().onEvent(" tabartouch","品牌");
				Logger.GetInstance().Debug("AllShopActivity ClickBrandBtn");
				jumpToBrandActivity();
				break;
			case R.id.cate:
				UserTrackMgr.getInstance().onEvent(" tabartouch","类别");
				Logger.GetInstance().Debug("AllShopActivity ClickCateBtn");
				jumpToCateActivity();
				break;
			case R.id.scan:
				UserTrackMgr.getInstance().onEvent(" tabartouch","扫码");
				Logger.GetInstance().Debug("AllShopActivity ClickScanBtn");
				jumpToScanActivity();
				break;
			case R.id.shopcat:
				UserTrackMgr.getInstance().onEvent(" tabartouch","购物车");
				Logger.GetInstance().Debug("AllShopActivity ClickShopcatBtn");
				jumpToShopcatActivity();
				break;
			case R.id.backbtn:
				Intent it = new Intent();
				it.setClass(AllShopActivity.this, SalerMainPageActivity.class);
				startActivity(it);
				break;
			case R.id.go_privilege_wrap:
				UserTrackMgr.getInstance().onEvent(" tabartouch","销售员查询");
				Intent intent = new Intent();
				intent.setClass(AllShopActivity.this, SalerMainPageActivity.class);
				startActivity(intent);
		}
	}
	private void unregistSaler() {
		 final Dialog dialog = new AlertDialog.Builder(this).create();
		 LayoutInflater inflater = LayoutInflater.from(this);
		 RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.allshop_salerlogout_dialog, null);
		 dialog.show();
		 dialog.getWindow().setContentView(layout);
		 dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		 dialog.getWindow().setLayout(880, RelativeLayout.LayoutParams.WRAP_CONTENT);
         dialog.setCancelable(false);
         TextView exitContent=(TextView)layout.findViewById(R.id.warnning_content);
         exitContent.setText("确认退出账号  "+ClosingRefInfoMgr.getInstance().getSalerInfo().getUserName()+"  " +ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerName()+"?");
         Button exitBtn = (Button) layout.findViewById(R.id.summit_btn);
         exitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent it = new Intent();
				it.setClass(AllShopActivity.this, SalerLoginActivity.class);
				startActivity(it);
				finish();
			}
		});
         Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
         cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void jumpToShopcatActivity() {
		Intent it = new Intent();
		it.setClass(this, ShopCartActivity.class);
		startActivity(it);
	}
	private void jumpToScanActivity() {
		Intent it = new Intent();
		it.setClass(this, CaptureActivity.class);
		startActivity(it);
	}
	private void jumpToCateActivity() {
		Intent it = new Intent();
		it.setClass(this, CategoryActivity.class);
		startActivity(it);
	}
	private void jumpToBrandActivity() {
		Intent it = new Intent();
		it.setClass(this, BrandActivity.class);
		startActivity(it);
	}

	public void startProgress() {
		mProgressBar.startProgress();
	}
	public void disProgress() {
		mProgressBar.stopProgress();
	}

	@Override
	public void isPrivilegeUser() {
		mTop.setBackgroundResource(R.drawable.allshop_privilege_bg);
		privilege_icon.setVisibility(View.VISIBLE);
		privilege_tv.setVisibility(View.VISIBLE);
		shopName.setTextColor(Color.rgb(255,255,255));
		salerName.setTextColor(Color.rgb(255,255,255));
		hndfBtn.setBackgroundResource(R.drawable.allshop_privilege_nhdf);
	}

	@Override
	public void isNotPrivilegeUser() {
		mTop.setBackgroundResource(R.color.ffffff);
		privilege_tv.setVisibility(View.GONE);
		privilege_icon.setVisibility(View.GONE);
		shopName.setTextColor(Color.rgb(238,118,26));
		salerName.setTextColor(Color.rgb(153,153,153));
		hndfBtn.setBackgroundResource(R.drawable.hhyglogo);
	}

	@Override
	public void isPrivilegeExceptionView(){

	}

	class FirstView implements AllShopView{
		@Override
		public void startProgress() {
			AllShopActivity.this.startProgress();
		}

		@Override
		public void disProgress() {
			AllShopActivity.this.disProgress();
		}

		@Override
		public void setAllShopContent() {
			AllShopActivity.this.setAllShopCreatContent();
			postable = true;
		}
		@Override
		public void setExceptionContent() {
			AllShopActivity.this.finish();
		}
	};
	class RestartView implements AllShopView{
		@Override
		public void startProgress() {
			AllShopActivity.this.startProgress();
		}

		@Override
		public void disProgress() {
			AllShopActivity.this.disProgress();
		}

		@Override
		public void setAllShopContent() {
			AllShopActivity.this.setAllShopCreatContent();
			postable = true;
		}

		@Override
		public void setExceptionContent() {
			setContent();
			postable = true;
		}

	}
	private void setAllShopCreatContent() {
		Logger.GetInstance().Debug("AllShopActivity SetContent");
		setContent();
	}

	private void setContent(){
		for(AllShopBaseFragment frag:mFragList){
			frag.setLastestContent();
		}
	}

}

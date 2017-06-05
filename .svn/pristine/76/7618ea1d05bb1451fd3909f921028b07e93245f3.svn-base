package com.hhyg.TyClosing.ui;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.RecommendAdapter;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.ReCommendInfo;
import com.hhyg.TyClosing.allShop.proc.ActiveInfoProc;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.fragment.GoodItemClickListener;
import com.hhyg.TyClosing.global.HttpUtil;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SPScrollview;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.ui.view.TabPageIndicator;
import com.hhyg.TyClosing.ui.view.TabPageIndicator.OnTabReselectedListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialActivity extends Activity{
	private HttpUtil mHttpUtil = MyApplication.GetInstance();
	private final String SPECIAL_URI = Constants.getIndexUrl()+"?r=topic/index";
	private SpecialProc mSpecialProc;
	private String HeadImgUri;
	private String subject;
	private ArrayList<ReCommendInfo> ReCommendInfos;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private ImageButton mBcakBtn;
	private TextView SubjectView;
	private ImageView HeadImgView;
	private ImageView mBacktoTop;
	private SPScrollview mScrollview;
	private ListView lv;
	private TabPageIndicator indicator;
	private TabPageIndicator topIndicator;
	private RecommendAdapter mAdapter;
	private ProgressBar mProgressBar;
	private ExceptionHandler exceptionHandler;
	private BroadcastReceiver mReceiver;
	/*专题ID*/
	private String specialId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special);
		findView();
		setSpecialId(savedInstanceState);
		init();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("theSpecialId", getIntentSpecialId());
		super.onSaveInstanceState(outState);
	}

	private void init(){
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		IntentFilter itF = new IntentFilter();
		itF.addAction(getResources().getString(R.string.privilege_unbind_receiver));
		registerReceiver(mReceiver,itF);
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		exceptionHandler = new ExceptionHandler(mProgressBar);
		ReCommendInfos = new ArrayList<ReCommendInfo>();
		mSpecialProc = new SpecialProc();
		mAdapter = new RecommendAdapter(this);
		mAdapter.setOnItemClickListener(new GoodItemClickListener(this));
		fetchLastestSpecial();
	}
	
	private void setSpecialId(Bundle savedInstanceState){
		if(savedInstanceState != null){
			specialId = savedInstanceState.getString("the specialId");
		}else{
			specialId = getIntentSpecialId();
		}
	}
	private void fetchLastestSpecial() {
		mProgressBar.startProgress();
		mHttpUtil.post(SPECIAL_URI,makeNetParam(), new NetCallBackHandlerException(exceptionHandler,mSpecialProc) );
	}
	private String makeNetParam(){
		JSONObject param = new JSONObject();
		try {
			param.put("channel", "体验店");
			param.put("specialid", specialId);
			param.put("imei", MyApplication.GetInstance().getAndroidId());
			param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
			param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return param.toString(); 		
	}
	private void findView() {
		mBacktoTop = (ImageView) findViewById(R.id.backtotop);
		SubjectView = (TextView) findViewById(R.id.specialname);
		HeadImgView = (ImageView) findViewById(R.id.headimg);
		mBcakBtn = (ImageButton) findViewById(R.id.backbtn);
		mBcakBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lv = (ListView) findViewById(R.id.listlv);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		topIndicator = (TabPageIndicator) findViewById(R.id.topindicator);
		mScrollview = (SPScrollview) findViewById(R.id.scrollview);
		mScrollview.setListener(scrollListener);
		mBacktoTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mScrollview.smoothScrollTo(0, 0);
			}
		});
		topIndicator.setOnTabReselectedListener(mTabListener);
		indicator.setOnTabReselectedListener(mTabListener);
	}
	
	private SPScrollview.OnScrollChangedListener scrollListener = new SPScrollview.OnScrollChangedListener() {
		@Override
		public void onScrollChanged(int dy) {
			if(topIndicator != null){
			if(dy >= (HeadImgView.getHeight())){
				topIndicator.setVisibility(View.VISIBLE);
			}else{
				topIndicator.setVisibility(View.GONE);
			}
			int totalHeight = 0;
			boolean set = false;
			for(int childIdx = 0; childIdx < lv.getChildCount(); childIdx++){
				View childView = lv.getChildAt(childIdx);
				totalHeight += childView.getHeight();
				if(dy >= totalHeight){
					indicator.setCurrentItem(childIdx + 1);
					topIndicator.setCurrentItem(childIdx + 1);
					set = true;
				}
				if( !set ){
					indicator.setCurrentItem(0);
					topIndicator.setCurrentItem(0);
				}
			}
		}
		}
	};
	private OnTabReselectedListener mTabListener = new TabPageIndicator.OnTabReselectedListener() {
		@Override
		public void onTabReselected(int position) {
			int dy = 0;
			for(int idx = 0 ; idx < position ; idx ++){
				View child = lv.getChildAt(idx);
				dy += child.getHeight() + 30;
			}
			dy = dy + HeadImgView.getHeight();
			if(position == 0){
				mScrollview.smoothScrollTo(0, 0);
			}else{
				mScrollview.smoothScrollTo(0, dy);
			}
		}
	};
	private String getIntentSpecialId(){
		Intent it = getIntent();
		String specialid = it.getStringExtra("specialid");
		return specialid;
	}
	private void showSpecialContent(){
		showHead();
		showFragContent();
		mProgressBar.stopProgress();
		mScrollview.smoothScrollTo(0, 0);
		mBacktoTop.setVisibility(View.VISIBLE);
	}
	private void showFragContent() {
		mAdapter.addItem(ReCommendInfos);
		lv.setAdapter(mAdapter);
		indicator.setResource(ReCommendInfos);
		indicator.setListView(lv);
		indicator.setVisibility(View.VISIBLE);
		topIndicator.setResource(ReCommendInfos);
		topIndicator.setListView(lv);
	}
	private void showHead(){
		SubjectView.setText(subject);
		ImageLoader.getInstance().displayImage(HeadImgUri, HeadImgView,ImageHelper.initSpecialPathOption());
	}
	class SpecialProc implements ProcMsgHelper{
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject CurjsonObj = JSONObject.parseObject(msgBody);
			try{
			JSONObject jsonObj = CurjsonObj.getJSONObject("data");
			JSONArray data = jsonObj.getJSONArray("data");
			HeadImgUri = jsonObj.getString("img_url");
			subject = jsonObj.getString("subject");
			for(int idx = 0;idx<data.size();idx++){
				JSONObject json = data.getJSONObject(idx);
				ReCommendInfo info = new ReCommendInfo();
				info.RecommendTile = json.getString("title");
				JSONArray array = json.getJSONArray("goods");
				ArrayList<GoodItemInfo> curResult = new ArrayList<GoodItemInfo>();
				for(int idx1 = 0;idx1<array.size();idx1++){
					JSONObject jsonobj = array.getJSONObject(idx1);
					GoodItemInfo goodInfo = new GoodItemInfo();
					goodInfo.barCode = jsonobj.getString("barcode");
					goodInfo.activeCut = jsonobj.getString("active_cut");
					goodInfo.activePrice= jsonobj.getString("active_price");
					goodInfo.brandName = jsonobj.getString("brandname");
					goodInfo.name = jsonobj.getString("name");
					goodInfo.full = jsonobj.getString("full");
					goodInfo.citPrice = jsonobj.getString("price");
					goodInfo.netUri = jsonobj.getString("image");
					goodInfo.stock = jsonobj.getInteger("stock");
					JSONObject aInfo_JOBJ = jsonobj.getJSONObject("activeinfo");
					if(aInfo_JOBJ != null){
						ActiveInfo aInfo = ActiveInfoProc.procActiveInfo(aInfo_JOBJ);
						goodInfo.activeInfo = aInfo;
					}
					curResult.add(goodInfo);
				}
				info.GoodList = curResult;
				if(info.GoodList.size() != 0){
					ReCommendInfos.add(info);
				}
			}
			}catch (ClassCastException e){
				exceptionHandler.sendEmptyMessage(5);
			}
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					showSpecialContent();
				}
			});
		}
	}
	private static class ExceptionHandler extends SimpleHandler{
		WeakReference<ProgressBar> ref;
		public ExceptionHandler(ProgressBar bar) {
			ref = new WeakReference<ProgressBar>(bar);
		}
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			final ProgressBar bar = ref.get();
			if(bar != null){
				bar.stopProgress();
			}
			if(msg.what == 4){ //取服务器错误信息
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
			}
			if(msg.what == 5){ //专题未配置data 为空
				Toast.makeText(MyApplication.GetInstance(), "该专题未配置", Toast.LENGTH_SHORT).show();
			}
		}
	}
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SpecialActivity");
        Logger.GetInstance().Track("SpecialActivity on onResume");
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("SpecialActivity");
	}

}

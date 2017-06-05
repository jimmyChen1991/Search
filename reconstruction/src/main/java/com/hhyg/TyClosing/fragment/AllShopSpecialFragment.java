package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.SpecialInfo;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.SpecialActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class AllShopSpecialFragment extends AllShopBaseFragment implements OnPageChangeListener, View.OnClickListener {

	private ViewPager mViewPager;
	private LinearLayout mTipGroup;
	private ArrayList<SpecialInfo> mSliderList;
	private ArrayList<SpecialInfo> mAdList;
	private ImageView AdOne;
	private ImageView AdTwo;
	private ImageView[] mTips;
	private final int AUTO_MSG = 1;
	private final int HANDLE_MSG = AUTO_MSG + 1;
	private static final int PHOTO_CHANGE_TIME = 3000;// 定时变量
	private int index = 0;
	private boolean mAutoMsgSendble;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
            if (mHandler.hasMessages(AUTO_MSG)){  
            	mHandler.removeMessages(AUTO_MSG);  
            }  
			switch (msg.what) {
			case AUTO_MSG:
				if(!mAutoMsgSendble){
					return;
				}
				if (index == mSliderList.size()) {
					index = 0;
					}
				mViewPager.setCurrentItem(index++);
				// 收到消息后设置当前要显示的图片
				mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
				break;
			case HANDLE_MSG:
				mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
				break;
			default:
				break;
			}
		};
	};
	private void setCanAutoMsgSend(boolean bool){
		mAutoMsgSendble = bool;
	}
	public void stopAutoMsgSend(){
		mHandler.removeMessages(AUTO_MSG);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allshop_special_frag, container);
		findView(view);
		return view;
	}
	@Override
	public void setLastestContent() {
		setCanAutoMsgSend(true);
		mSliderList = mAllShopInfoMgr.getAllShopInfo().sliderInfoList;
		mAdList = mAllShopInfoMgr.getAllShopInfo().AdInfoList;
		initTips(mSliderList.size());
		MyAdapter adapter = new MyAdapter();
		mViewPager.setAdapter(adapter);
		mHandler.sendEmptyMessageDelayed(AUTO_MSG, PHOTO_CHANGE_TIME);
		if(mAdList.size()<2){
			return;
		}
		AdSetTag();
		showAd();
	}
	private void showAd() {
		ImageLoader.getInstance().displayImage(mAdList.get(0).netUri, AdOne, ImageHelper.initSpecialPathOption());
		ImageLoader.getInstance().displayImage(mAdList.get(1).netUri, AdTwo, ImageHelper.initSpecialPathOption());
	}
	private void AdSetTag() {
		AdOne.setTag(mAdList.get(0).id);
		AdTwo.setTag(mAdList.get(1).id);
	}
	private void findView(View root) {
		mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
		mViewPager.addOnPageChangeListener(this);
		mTipGroup = (LinearLayout) root.findViewById(R.id.viewGroup);
		AdOne = (ImageView) root.findViewById(R.id.adone);
		AdTwo = (ImageView) root.findViewById(R.id.adtwo);
		AdOne.setOnClickListener(this);
		AdTwo.setOnClickListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0);
	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < mTips.length; i++) {
			if (i == selectItems) {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mSliderList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView view = new ImageView(getActivity());
			String uri = mSliderList.get(position).netUri;
			String specialId = mSliderList.get(position).id;
			((ViewPager) container).addView(view, 0);
			view.setTag(specialId);
			view.setScaleType(ScaleType.FIT_XY);
			view.setOnClickListener(AllShopSpecialFragment.this);
			ImageAware imageAware = new ImageViewAware(view, false);
			ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initSpecialPathOption());
			return view;
		}
	}

	private void initTips(int pointCnt) {
		mTips = null;
		mTipGroup.removeAllViews();
		mTips = new ImageView[pointCnt];
		Logger.GetInstance().Debug("size"+pointCnt);
		for (int i = 0; i < mTips.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			ll.setMargins(15, 0, 15, 0);
			imageView.setLayoutParams(ll);
			mTips[i] = imageView;
			if (i == 0) {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			mTipGroup.addView(imageView);
		}
	}

	@Override
	public void onClick(View v) {
		UserTrackMgr.getInstance().clear();
		UserTrackMgr.getInstance().enter("AllShopSpecialFragment");
        UserTrackMgr.getInstance().onEvent("adtolist", (String) v.getTag());
		String SpecialId = (String) v.getTag();
		jumpToSpecialActivity(SpecialId);
	}

	private void jumpToSpecialActivity(String specialId) {
		Intent intent = new Intent(getActivity(), SpecialActivity.class);
		intent.putExtra("specialid", specialId);
		startActivity(intent);
	}

}

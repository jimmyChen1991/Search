package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.FlashSaleInfo;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.ui.SpecialActivity;
import com.hhyg.TyClosing.ui.view.ActiviteFlashView;
import com.hhyg.TyClosing.ui.view.BaseFlashView;
import com.hhyg.TyClosing.ui.view.FullReduceFalseView;
import com.hhyg.TyClosing.ui.view.NormalFalshView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AllShopFlashSaleFragment extends AllShopBaseFragment{
	private LinearLayout mImgGroup;
	private FlashSaleInfo mFlashSaleInfo;
	private GoodItemClickListener mGoodInfoPresenter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allshop_flashsale_frg, container);
		findView(view);
		mGoodInfoPresenter = new GoodItemClickListener(getActivity());
		return view;
	}
	@Override
	public void setLastestContent() {
		mFlashSaleInfo = mAllShopInfoMgr.getAllShopInfo().flashInfo;
		loadView();
	}
	private void findView(View root){
		mImgGroup = (LinearLayout) root.findViewById(R.id.viewGroup);
	}
	private void loadView(){
		mImgGroup.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); // ÿ�е�ˮƽLinearLayout
     	layoutParams.setMargins(10, 0, 10, 0); 
     	int count = mFlashSaleInfo.GoodList.size();
		for(int idx = 0;idx<count;idx++){
			final GoodItemInfo info = mFlashSaleInfo.GoodList.get(idx);
			String uri = "";			
			LinearLayout.LayoutParams itemParams = layoutNormalItemParamsCreator();
			if(idx == 0){
				itemParams.setMargins(20, 0, 0, 0);
			}
			
			BaseFlashView view = null;
			final ActiveInfo aInfo = info.activeInfo;
			if(aInfo == null || aInfo.getType() == null || aInfo.getType() == ActiveType.Normal ){
				view = new NormalFalshView(getActivity());
				NormalFalshView nView = (NormalFalshView) view;
				nView.setCitPrice(info.citPrice);
			}else if(aInfo.getType() == ActiveType.Cut){
				view = new ActiviteFlashView(getActivity());
				ActiviteFlashView aView = (ActiviteFlashView) view;
				aView.setActivitePrice(aInfo.getActive_price());
				aView.setCitPrice(info.citPrice);
				aView.setActiviteIndicator4Cut(aInfo.getShort_desc());
			}else {
				view = new FullReduceFalseView(getActivity());
				FullReduceFalseView fullView = (FullReduceFalseView) view;
				fullView.setCitPrice(info.citPrice);
				fullView.setActiviteIndicator4FullReduce(aInfo.getShort_desc());
			}
			if(aInfo != null && aInfo.getPrivilegeType() == ActiveInfo.PrivilegeType.PRIVILEGE_TYPE){
				view.setPrivilegeIconVisible();
			}else{
				view.setPrivilegeIconGone();
			}
			if(info.stock == 0){
				view.setNoStockImgVisible();
			}
			String str = info.brandName;
			if(str.length()>10){
				str = info.brandName.substring(0, 10)+"...";
			}
			view.setBrandName(str);
			view.setName(info.name);
			view.setLayoutParams(itemParams);
			uri = info.netUri;
			ImageAware imageAware = new ImageViewAware(view.getImagewView(), false);
			ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption(),new SimpleImageLoadingListener(){
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {					
					}
				});
			view.setTag(info.barCode);
			view.setOnClickListener(mGoodInfoPresenter);
			mImgGroup.addView(view);
			}
		loadGetMoreBtn();
	}
	private LinearLayout.LayoutParams layoutNormalItemParamsCreator(){
		LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(280, LinearLayout.LayoutParams.WRAP_CONTENT);
		itemParams.setMargins(60, 0, 0, 0);
		return itemParams;
	}
	private LinearLayout.LayoutParams layoutGetMoreBtnItemParamsCreator(){
		LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(280, LinearLayout.LayoutParams.WRAP_CONTENT);
		itemParams.gravity = Gravity.CENTER;
		return itemParams;
	}
	private void loadGetMoreBtn(){
		ImageButton FlashGetMoreBtn = new ImageButton(getActivity());
		FlashGetMoreBtn.setBackgroundResource(R.drawable.allshop_flashgetmore);
		FlashGetMoreBtn.setTag(mFlashSaleInfo.specialId);
		FlashGetMoreBtn.setLayoutParams(layoutGetMoreBtnItemParamsCreator());
		FlashGetMoreBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToSpecialActivity((String) v.getTag());
			}
		});
		mImgGroup.addView(FlashGetMoreBtn);
	}
	private void jumpToSpecialActivity(String specialId){
		Intent intent = new Intent(getActivity(), SpecialActivity.class);
		intent.putExtra("specialid", specialId);
		startActivity(intent);
	}
}

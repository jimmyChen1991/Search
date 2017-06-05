package com.hhyg.TyClosing.ui.view;

import com.hhyg.TyClosing.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseFlashView extends RelativeLayout{
	protected ImageView mImageView;
	protected TextView mBrandName;
	protected TextView mName;
	protected ImageView mNoStock;
	protected ImageView mPrivilegeIcon;
	protected TextView mActiviteIndicator;
	protected RelativeLayout mPriceLayout,mImgLayout;
	protected RelativeLayout.LayoutParams mImgParams,mBrandNameParams,mNameParams,mPricePairParams,mNoStockImgParams,mImgLayoutParams,mActiviteIndicatorParams,mPrivilegeIconParam;
	public BaseFlashView(Context context) {
		super(context);
		initImgLayout(context);
		initBrandName(context);
		initName(context);
		initPriceParam(context);
		initActiviteIndicator(context);
		initPrivilegeParam(context);
	}

	private void initPrivilegeParam(Context context){
		mPrivilegeIcon = new ImageView(context);
		mPrivilegeIcon.setBackgroundResource(R.drawable.shopcart_privilege_icon);
		mPrivilegeIconParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mPrivilegeIconParam.addRule(RelativeLayout.BELOW,R.id.flash_indictor);
		mPrivilegeIconParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mPrivilegeIconParam.setMargins(0,10,0,0);
		mPrivilegeIcon.setLayoutParams(mPrivilegeIconParam);
		mImgLayout.addView(mPrivilegeIcon);
	}

	private void initActiviteIndicator(Context context) {
		mActiviteIndicator = new TextView(context);
		mActiviteIndicator.setTextColor(getResources().getColor(R.color.maincolor));
		mActiviteIndicator.setBackgroundResource(R.drawable.btnshape);
		mActiviteIndicator.setId(R.id.flash_indictor);
		mActiviteIndicatorParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mActiviteIndicatorParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mActiviteIndicator.setTextSize(15);
		mActiviteIndicator.setPadding(10,3,10,3);
		mActiviteIndicator.setGravity(Gravity.CENTER);
		mActiviteIndicator.setLayoutParams(mActiviteIndicatorParams);
		mActiviteIndicator.setVisibility(View.GONE);
		mImgLayout.addView(mActiviteIndicator);
	}
	private void initNoStock(Context context) {
		mNoStock = new ImageView(context);
		mNoStockImgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mNoStockImgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mNoStock.setLayoutParams(mNoStockImgParams);
		mNoStock.setBackgroundResource(R.drawable.allshop_nostock);
		mNoStock.setVisibility(View.GONE);
		mImgLayout.addView(mNoStock);
	}
	public void setPrivilegeIconVisible(){ mPrivilegeIcon.setVisibility(VISIBLE);}
	public void setPrivilegeIconGone(){ mPrivilegeIcon.setVisibility(GONE);}
	public void setNoStockImgVisible(){
		mNoStock.setVisibility(View.VISIBLE);
	}
	public ImageView getImagewView(){
		return mImageView;
	}
	public void setBrandName(String text){
		mBrandName.setText(text);
	}
	public void setName(String text){
		String name = "";
		if(text.length()>10){
			name = text.substring(0, 10)+"...";
		}else{
			name = text;
		}
		mName.setText(name);
	}
	private void initImgLayout(Context context){
		mImgLayout = new RelativeLayout(context);
		mImgLayoutParams = new RelativeLayout.LayoutParams(240,240);
		mImgLayoutParams.setMargins(0, 20, 0, 0);
		mImgLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mImgLayout.setId(R.id.flash_imglayout);
		mImgLayout.setLayoutParams(mImgLayoutParams);
		initImgView(context);
		initNoStock(context);
		addView(mImgLayout);
	}
	private void initImgView(Context context){
		mImageView = new ImageView(context);
		mImgParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mImageView.setLayoutParams(mImgParams);
		mImgLayout.addView(mImageView);
	}
	private void initBrandName(Context context){
		mBrandName = new TextView(context);
		mBrandName.setTextColor(Color.rgb(102, 102, 102));
		mBrandName.setTextSize(12);
		mBrandName.setId(R.id.flash_brandname);
		mBrandNameParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mBrandNameParams.addRule(RelativeLayout.BELOW, R.id.flash_imglayout);
		mBrandNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mBrandName.setLayoutParams(mBrandNameParams);
		addView(mBrandName);
	}
	private void initName(Context context){
		mName = new TextView(context);
		mName.setTextColor(Color.rgb(102, 102, 102));
		mName.setTextSize(12);
		mName.setId(R.id.flash_name);
		mNameParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mNameParams.addRule(RelativeLayout.BELOW, R.id.flash_brandname);
		mNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mName.setLayoutParams(mNameParams);
		addView(mName);
	}
	private void initPriceParam(Context context){
		mPriceLayout = new RelativeLayout(context);
		mPricePairParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		mPricePairParams.addRule(RelativeLayout.BELOW, R.id.flash_name);
		mPricePairParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mPriceLayout.setLayoutParams(mPricePairParams);
		addView(mPriceLayout);
	}
}

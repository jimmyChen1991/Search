package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhyg.TyClosing.R;

public class ActiviteFlashView extends BaseFlashView{
	private LayoutParams mActivitePriceParams;
	private LayoutParams mCitPriceParams;
	private TextView mActivitePrice;
	private TextView mCitPrice;
	public ActiviteFlashView(Context context) {
		super(context);
		initActivitePrice(context);
		initCitPrice(context);
	}
	public void setActivitePrice(String price){
		mActivitePrice.setText("¥"+price);
	}
	public void setCitPrice(String price){
		mCitPrice.setText("¥"+price);
	}
	public void setActiviteIndicator4Cut(String cut){
		mActiviteIndicator.setVisibility(View.VISIBLE);
		mActiviteIndicator.setText(cut);
	}
	private void initActivitePrice(Context context){
		mActivitePrice = new TextView(context);
		mActivitePrice.setTextColor(Color.rgb(195, 140, 86));
		mActivitePrice.setTextSize(12);
		mActivitePrice.setId(R.id.flash_activeprice);
		mActivitePriceParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mActivitePrice.setLayoutParams(mActivitePriceParams);
		mPriceLayout.addView(mActivitePrice);
	}
	private void initCitPrice(Context context){
		mCitPrice = new TextView(context);
		mCitPrice.setTextColor(Color.rgb(204, 204, 204));
		mCitPrice.setTextSize(12);
		mCitPrice.setId(R.id.flash_citprice);
		mCitPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG ); //中间横线
		mCitPrice.getPaint().setAntiAlias(true);// 抗锯齿
		mCitPriceParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mCitPriceParams.addRule(RelativeLayout.BELOW, R.id.flash_name);
		mCitPriceParams.addRule(RelativeLayout.RIGHT_OF, R.id.flash_activeprice);
		mCitPriceParams.setMargins(10, 0, 0, 0);
		mCitPrice.setLayoutParams(mCitPriceParams);
		mPriceLayout.addView(mCitPrice);
	}
}

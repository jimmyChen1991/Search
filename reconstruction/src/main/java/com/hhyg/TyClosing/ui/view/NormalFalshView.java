package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhyg.TyClosing.R;

public class NormalFalshView extends BaseFlashView{
	private TextView mCitPrice;
	private LayoutParams mCitPriceParams;
	public NormalFalshView(Context context) {
		super(context);
		initCitPrice(context);
	}
	public void setCitPrice(String price){
		mCitPrice.setText("¥"+price);
	}
	private void initCitPrice(Context context){
		mCitPrice = new TextView(context);
		mCitPrice.setTextColor(Color.rgb(204, 204, 204));
		mCitPrice.setTextSize(12);
		mCitPriceParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mCitPriceParams.addRule(RelativeLayout.BELOW, R.id.flash_name);
		mCitPrice.setLayoutParams(mCitPriceParams);
		mPriceLayout.addView(mCitPrice);
	}
}

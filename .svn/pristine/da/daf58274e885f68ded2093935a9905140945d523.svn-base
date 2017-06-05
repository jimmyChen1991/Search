package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhyg.TyClosing.R;

public class ImageBrand extends RelativeLayout{
	private ImageView mImageView;
	private TextView mBrandName;
	private TextView mSlipt;
	private RelativeLayout.LayoutParams mImgParams,mNameParams,mSliptParams;
	
	public ImageBrand(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ImageBrand(Context context) {
		super(context);
		mImageView = new ImageView(context);
		mBrandName = new TextView(context);
		mSlipt = new TextView(context);
		mImgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mImgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mImageView.setLayoutParams(mImgParams);
		mImageView.setId(R.id.imagebrand_image);
		mSliptParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
		mSliptParams.addRule(RelativeLayout.BELOW, R.id.imagebrand_image);
		mSlipt.setLayoutParams(mSliptParams);
		mSlipt.setBackgroundColor(Color.rgb(102, 102, 102));
		mSlipt.setId(R.id.imagebrand_split);
		mNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		mNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mNameParams.addRule(RelativeLayout.BELOW,R.id.imagebrand_split);
		mBrandName.setLayoutParams(mNameParams);
		this.addView(mImageView);
		this.addView(mSlipt);
		this.addView(mBrandName);
	}
	public void setBrandName(String name){
		mBrandName.setText(name);
	}
	public ImageView getImageView(){		
		return mImageView;
	}
}

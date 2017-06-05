package com.hhyg.TyClosing.ui.view;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.mgr.HotSearchListener;

import android.app.Activity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AllShopTopEdit implements TopEdit{
	private EditText mEditText;
	private ImageView searchIcon;
	private ImageView searchBlank;
	private ImageButton BackBtn;
	private Activity context;
	private OnClickListener iconListener;
	private OnClickListener backListener;
	@Override
	public void setIconListener(OnClickListener l){
		iconListener = l;
	}
	@Override
	public void setBackListener(View.OnClickListener l){
		backListener = l;
	}
	public AllShopTopEdit(Activity c) {
		context = c;
	}
	@Override
	public void topEdit() {
		if(iconListener == null){
			iconListener = new HotSearchListener();
		}
		if(backListener == null){
			backListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.finish();
				}
			};
		}
		mEditText = (EditText) context.findViewById(R.id.searchbar);
		searchIcon = (ImageView) context.findViewById(R.id.searchicon);
		searchBlank = (ImageView) context.findViewById(R.id.searchblank);
		BackBtn = (ImageButton) context.findViewById(R.id.backbtn);
		mEditText.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mEditText.setInputType(InputType.TYPE_NULL); 
				return false;
			}
		});
		searchIcon.setOnClickListener(iconListener);
		mEditText.setOnClickListener(iconListener);
		searchBlank.setOnClickListener(iconListener);
		BackBtn.setOnClickListener(backListener);
	}

}

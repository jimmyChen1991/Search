package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class SPScrollview extends ScrollView{
	private OnScrollChangedListener mListener;
	public interface OnScrollChangedListener{
		void onScrollChanged(int dy);
	}
	public void setListener(OnScrollChangedListener listener) {
		mListener = listener;
	}
	public SPScrollview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SPScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SPScrollview(Context context) {
		super(context);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mListener != null){
			mListener.onScrollChanged(t);
		}
	}
}

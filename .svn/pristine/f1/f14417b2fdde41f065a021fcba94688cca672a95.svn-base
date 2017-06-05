package com.hhyg.TyClosing.ui.view;

import java.lang.ref.WeakReference;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.MyApplication;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SimpleProgressBar implements ProgressBar{
	WeakReference<ImageView> ref;
	Animation animation;
	public SimpleProgressBar(ImageView v) {
		ref = new WeakReference<ImageView>(v);
		setAnimation();
	}
	@Override
	public void startProgress() {
		ImageView view = ref.get();
		if(view != null){
			view.setVisibility(View.VISIBLE);
			view.startAnimation(animation);
		}
	}
	private void setAnimation() {
		animation= AnimationUtils.loadAnimation(MyApplication.GetInstance(), R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
	}
	@Override
	public void stopProgress() {
		ImageView view = ref.get();
		if(view != null){
			view.clearAnimation();
			view.setVisibility(View.GONE);
		}
	}
}

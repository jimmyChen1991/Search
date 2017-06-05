package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.view.View;

public class PrivilegeFlashView extends ActiviteFlashView{
	public PrivilegeFlashView(Context context) {
		super(context);
	}
	@Override
	public void setActiviteIndicator4Cut(String cut) {
		mActiviteIndicator.setVisibility(View.VISIBLE);
		mActiviteIndicator.setText("特权");
	}
}

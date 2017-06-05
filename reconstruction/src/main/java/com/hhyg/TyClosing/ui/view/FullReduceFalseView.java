package com.hhyg.TyClosing.ui.view;

import android.content.Context;
import android.view.View;

public class FullReduceFalseView extends NormalFalshView{
	public FullReduceFalseView(Context context) {
		super(context);
	}
	public void setActiviteIndicator4FullReduce(){
		mActiviteIndicator.setVisibility(View.VISIBLE);
		mActiviteIndicator.setText("满减");
	}
	
	public void setActiviteIndicator4FullReduce(String str){
		mActiviteIndicator.setVisibility(View.VISIBLE);
		mActiviteIndicator.setText(str);
	}
}

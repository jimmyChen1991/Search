package com.hhyg.TyClosing.allShop.handler;

import com.hhyg.TyClosing.view.BaseView;

import android.os.Message;

public class SimpleProgressHandler<V extends BaseView> extends SimpleHandler{
	private V mView;
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(mView != null){
			mView.disProgress();
		}
	}
	public void attchView(V view){
		mView = view;
	}
	public void dettch(){
		mView = null;
	}
}

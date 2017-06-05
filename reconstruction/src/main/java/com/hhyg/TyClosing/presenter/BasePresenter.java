package com.hhyg.TyClosing.presenter;

import com.hhyg.TyClosing.allShop.handler.SimpleProgressHandler;
import com.hhyg.TyClosing.global.HttpUtil;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.view.BaseView;

import android.os.Handler;
import android.os.Looper;

public abstract class BasePresenter<T extends BaseView>{
	protected T mView;	
	protected Handler mHandler = new Handler(Looper.getMainLooper());
	protected HttpUtil mHttpRequester = MyApplication.GetInstance();
	protected SimpleProgressHandler<T> mSimpleProgressHandler = new SimpleProgressHandler<T>();
	public void attach(T view){
		mView = view;
		mSimpleProgressHandler.attchView(view);
	}
	public void detach(){
		mView = null;
		mSimpleProgressHandler.dettch();
	}
}

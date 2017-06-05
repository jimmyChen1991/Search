package com.hhyg.TyClosing.presenter;

import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.allShop.info.AllShopInfo;
import com.hhyg.TyClosing.allShop.mgr.AllShopInfoMgr;
import com.hhyg.TyClosing.allShop.proc.AllShopProc;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.view.AllShopView;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;


public class AllshopPresenter extends BasePresenter<AllShopView> implements AllShopProc.OnProcListener{	
	private final String ALL_SHOP_URL = Constants.getIndexUrl()+"?r=tyhomeapi/home";	
	private AllShopProc mAllShopProc;
	public AllshopPresenter(){		
		super();
		mAllShopProc = new AllShopProc();
		mAllShopProc.setOnProcListener(this);
	}
	public void fetchLastestAllShopData(){
		mView.startProgress();
		fetchAllShopData(new LocalHandler());
	}
	public void fetLatestAllShopDataOnRestart(){
		mView.startProgress();
		fetchAllShopData(new LocalHandler());
	}
	private void fetchAllShopData(Handler handler){
		mHttpRequester.post(ALL_SHOP_URL, JsonPostParamBuilder.makeParam(), new NetCallBackHandlerException(handler,mAllShopProc));
	}
	@Override
	public void OnProcCompleted() {
		AllShopInfo info = mAllShopProc.getAllShopInfo();
		AllShopInfoMgr.getInstance().setAllShopInfo(info);
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(mView != null){
					mView.setAllShopContent();
					mView.disProgress();
				}
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	class LocalHandler extends SimpleHandler{
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if(mView != null){
				mView.setExceptionContent();
				mView.disProgress();
			}
		}
	}
}

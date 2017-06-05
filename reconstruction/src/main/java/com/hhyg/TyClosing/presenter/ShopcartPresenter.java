package com.hhyg.TyClosing.presenter;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.allShop.proc.ShopCartProc;
import com.hhyg.TyClosing.allShop.proc.ShopCartProc.OnProcListener;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.view.ShopCartView;

import android.os.Message;

public class ShopcartPresenter extends BasePresenter<ShopCartView> implements OnProcListener{
	
	private String SHOPCART_URL = Constants.getIndexUrl() + "?r=cart/padcartlist";
	private ArrayList<ShoppingCartInfo> mInfoList = ShoppingCartMgr.getInstance().getAll();
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	private ShopCartProc mProc;
	
	public ShopcartPresenter() {
		mProc = new ShopCartProc();
	}
	public synchronized void fetchShopCartColumns(){
		mProc.setListener(this);
		if(mInfoList == null || mInfoList.size() == 0){
			String zero = "0";
			ShoppingCartMgr.getInstance().setDiscount(zero);
			ShoppingCartMgr.getInstance().setRealCast(zero);
			ShoppingCartMgr.getInstance().setTotalPrice(zero);
			if(mView != null){
				mView.emptyShopcart();
			}
			return;
		}
		if(mView != null){
			mView.startProgress();
		}
		mHttpRequester.post(SHOPCART_URL, makeJsonString(), new NetCallBackHandlerException(new Exhandler(), mProc));
	}
	
	class Exhandler extends SimpleHandler{
		@Override
		public void handleMessage(Message msg) {
			String zero = "0";
			ShoppingCartMgr.getInstance().setDiscount(zero);
			ShoppingCartMgr.getInstance().setRealCast(zero);
			ShoppingCartMgr.getInstance().setTotalPrice(zero);
			super.handleMessage(msg);
			if(mView != null){
				mView.exceptionCart();
				mView.disProgress();
			}
		}
	}
	
	protected String makeJsonString() {
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		param.put("saler_id", ClosingRefInfoMgr.getInstance().getSalerId());
		param.put("deliverplace", ClosingRefInfoMgr.getInstance().getCurPickupId());
		param.put("platformid",3);
		JSONArray dataArr = new JSONArray();
		for(ShoppingCartInfo cartInfo : mInfoList){
			JSONObject jobj = new JSONObject();
			jobj.put("barcode", cartInfo.barCode);
			jobj.put("number", cartInfo.cnt);
			if(cartInfo.activeId != null){
				jobj.put("activity", cartInfo.activeId);
			}else{
				jobj.put("activity","");
			}
			dataArr.add(jobj);
		}
		param.put("data", dataArr);
		return param.toString();
	}
	
	@Override
	public void OnProcCompleted() {
		if(mView != null ){
			if(mShoppingCartMgr.getColumns() != null || mShoppingCartMgr.getColumns().size() !=0){
				if(mShoppingCartMgr.isWarnning()){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mView.disProgress();
							mView.warnningCart();
						}
					});
					return;
				}
				
				if(mShoppingCartMgr.getColumns().size() == 1 && mShoppingCartMgr.getColumns().get(0).getaInfo().getType() == ActiveType.NoStock){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mView.disProgress();
							mView.onlyNoStock();
						}
					});
				}else{
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mView.disProgress();
							mView.shopcartList();
						}
					});
				}
			}
		}
	}
}

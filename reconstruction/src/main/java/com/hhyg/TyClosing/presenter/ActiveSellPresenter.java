package com.hhyg.TyClosing.presenter;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.adapter.ActiveGoodAdapter.ItemAddListener;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.allShop.proc.ActiveGoodProc;
import com.hhyg.TyClosing.allShop.proc.BaseActiveGoodProc;
import com.hhyg.TyClosing.allShop.proc.FirstActiveGoodProc;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.view.SearchGoodView;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
public class ActiveSellPresenter extends BasePresenter<SearchGoodView> implements ItemAddListener{
	private SearchInfo mSearchInfo;
	private String SEARCHGOOD_URI;
	private String mActId;
	private ActiveGoodProc mSessionSearchGoodProc;
	private FirstActiveGoodProc mFirstSearchGoodPrco;
	private SearchResultInfo SearchResult;
	private SearchGoodMgr mSearchGoodMgr;
	private UiHandler mUiHandler;
	private SrollUiHandler mSrollUiHandler;
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	private boolean isPrivilege;
	private String privilegeName;
	public ActiveSellPresenter(SearchGoodMgr mgr,String activeId,boolean isPrivilege) {
		super();
		mSearchGoodMgr = mgr;
		mActId = activeId;
		this.isPrivilege = isPrivilege;
		init();
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	@Override
	public void attach(SearchGoodView view) {
		super.attach(view);
	}
	@Override
	public void detach() {
		super.detach();
	}
	
	public void fetchFirstSearchGood(){
		mView.startProgress();
		mSearchInfo = mSearchGoodMgr.searchInfo;
		fetchLaetedSearchResult(mUiHandler,mFirstSearchGoodPrco);
	}
	
	public void fetchData(){
		if(ShoppingCartMgr.getInstance().getAll() == null || ShoppingCartMgr.getInstance().getAll().size() == 0 ){
			if(mView != null){
				final String zero = "0";
				mView.setPrice(zero, zero, zero,null);
			}
			return;
		}else{
			JSONArray dataArr = new JSONArray();
			for(ShoppingCartInfo cartInfo : ShoppingCartMgr.getInstance().getAll()){
				if(cartInfo.activeId.equals(mActId)){
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
			}
			if(dataArr.size() == 0){
				if(mView != null){
					final String zero = "0";
					mView.setPrice(zero, zero, zero,null);
				}
				return;
			}
		}
		fetchPriceInfo();
	}
	
	public void fetchSessionSearchGood(){
		mView.startProgress();
		mSearchInfo = mSearchGoodMgr.searchInfo;
		fetchLaetedSearchResult(mUiHandler,mSessionSearchGoodProc);
	}
	public void fetchSrollSessionSearchGood(){
		mView.startProgress();
		mSearchInfo = mSearchGoodMgr.searchInfo;
		fetchLaetedSearchResult(mSrollUiHandler,mSessionSearchGoodProc);
	}
	
	private void fetchLaetedSearchResult(Handler handler,BaseActiveGoodProc proc){
		mHttpRequester.post(SEARCHGOOD_URI,MakeJsonString(), new NetCallBackHandlerException(handler,proc));
	}
	
	protected String MakeJsonString() {
		JSONObject param = new JSONObject();
			param.put("imei", MyApplication.GetInstance().getAndroidId());
			param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
			param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
			param.put("op", "activegoods");
			param.put("platformid",3);
			JSONObject data = new JSONObject();
			data.put("activityid", mActId);
			if(privilegeName != null){
				data.put("title",privilegeName);
			}
			if(mSearchInfo.cateId != null){
				data.put("cat_id", mSearchInfo.cateId);
			}
			if(mSearchInfo.brandId != null){
				data.put("brand_id", mSearchInfo.brandId);
			}
			if(mSearchInfo.priceInfo!=null){
				data.put("price_min", mSearchInfo.priceInfo.MinPrice);
				data.put("price_max", mSearchInfo.priceInfo.MaxPrice);
			}else{
				data.put("price_min", "0");
				data.put("price_max", "0");
			}
			if(mSearchInfo.sort == 0){
				if(mSearchInfo.sortfieId == 0){
					data.put("sort_field", 3);
				}else if(mSearchInfo.sortfieId == 1){
					data.put("sort_field", 2);
				}else if(mSearchInfo.sortfieId == 2){
					data.put("sort_field", 1);
				}
			}else if(mSearchInfo.sort == 1){
				if(mSearchInfo.sortfieId == 0){
					data.put("sort_field", -3);
				}else if(mSearchInfo.sortfieId == 1){
					data.put("sort_field", -2);
				}else if(mSearchInfo.sortfieId == 2){
					data.put("sort_field", -1);
				}
			}else{
				data.put("sort_field", 0);
			}
			data.put("returncateandbrand", mSearchInfo.isNeedData);
			if(mSearchInfo.sort != -1){
				data.put("sort", mSearchInfo.sort);
			}
			data.put("pagesize", Constants.PAGE_SIZE);
			data.put("currentpage", mSearchInfo.currerPage);
			param.put("data", data);
		return param.toString();
	}
	
	class UiHandler extends Handler{
		@Override
		public void dispatchMessage(Message msg) {
			switch(msg.what){
			case 0:
				if(mView != null){
					mView.setNullShopContent();
				}
				break;
			case 1:
				Toast.makeText(MyApplication.GetInstance(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case 4: 
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			}
			if(mView != null){
				mView.disProgress();
				mView.ResetChoseBtn();
			}
		}
	}
	class SrollUiHandler extends Handler{
		@Override
		public void dispatchMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(MyApplication.GetInstance(), "后台数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(MyApplication.GetInstance(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case 4: 
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			if(mView != null){
				mView.disProgress();
				mView.ResetChoseBtn();
				mView.setScollExceptionCanScoll();
			}
		}
	}
	class SearchSessionOnSearchListener implements BaseActiveGoodProc.OnSearchListener{
		@SuppressWarnings("unchecked")
		@Override
		public void OnSearchCompleted() {
			SearchResult = mSessionSearchGoodProc.getSearchResult();
			ArrayList<GoodItemInfo> goodResult = mSessionSearchGoodProc.getSearchGood();
			mSearchGoodMgr.GoodResult = (ArrayList<GoodItemInfo>) goodResult.clone();
			mSearchGoodMgr.searchResult = (SearchResultInfo) SearchResult;
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					if(mView != null){
						mView.disProgress();
						if(SearchResult == null || SearchResult.GoodsInfo == null ||SearchResult.GoodsInfo.size() == 0)
						{
							mView.setNullShopContent();
						}
						mView.setSessionShopContent();
					}
				}
			});
		}
	} 
	class SearchFirstOnSearchListener implements BaseActiveGoodProc.OnSearchListener{
		@SuppressWarnings("unchecked")
		@Override
		public void OnSearchCompleted() {
			SearchResult = mFirstSearchGoodPrco.getSearchResult();
			ArrayList<GoodItemInfo> goodResult = mFirstSearchGoodPrco.getSearchGood();
			mSearchGoodMgr.GoodResult = (ArrayList<GoodItemInfo>) goodResult.clone();
			mSearchGoodMgr.searchResult = (SearchResultInfo) SearchResult;
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					if(mView != null){
						if(mSearchGoodMgr.GoodResult.size() != 0){
							mView.setfirstShopContent();
						}
						else{
							mView.setNullShopContent();
						}
						mView.disProgress();
					}
				}
			});
		}
	}
	
	private void init(){
		SEARCHGOOD_URI = isPrivilege ? Constants.getIndexUrl() + "?r=active/privilegelist" : Constants.getIndexUrl()+"?r=active/activelist";
		mUiHandler = new UiHandler();
		mSrollUiHandler = new SrollUiHandler();
		mSessionSearchGoodProc = new ActiveGoodProc();
		mSessionSearchGoodProc.setOnSearchListener(new SearchSessionOnSearchListener());
		mFirstSearchGoodPrco = new FirstActiveGoodProc();
		mFirstSearchGoodPrco.setOnSearchListener(new SearchFirstOnSearchListener());
	}
	
	@Override
	public void onAdd(GoodItemInfo info) {
		final int choseCnt;
		if(!ShoppingCartMgr.getInstance().isInfoExist(info.barCode)){
			choseCnt = 1;
		}else{
			choseCnt = ShoppingCartMgr.getInstance().getInfoByBarCode(info.barCode).cnt +1;
		}
		if(choseCnt > info.stock){
			Toast.makeText(MyApplication.GetInstance(), "该商品库存不足", Toast.LENGTH_SHORT).show();
			return;
		}
		if (info.shelveStatus == 1 && info.stock > 0 ){
			SpuInfo spu = new SpuInfo();
			spu.barCode = info.barCode;
			spu.activeId = mActId;
			spu.name = info.name;
			spu.stock = info.stock;	
			spu.citAmount = info.citAmount;
			spu.attrInfo = info.attr;
			spu.msPrice = info.citPrice;
			if(info.netUri != null && !info.netUri.equals("")){
				ArrayList<String> imgLinks = new ArrayList<String>();
				imgLinks.add(info.netUri);
				spu.imageLinks = imgLinks;
			}
			if(mShoppingCartMgr.isInfoExist(info.barCode)){
				mShoppingCartMgr.updateShopCnt(info.barCode,choseCnt);
			}else{
				mShoppingCartMgr.addInfo(spu, info.name, info.brandName, 1, info.typeId, info.CitName);
			}
			if(!isPrivilege){
				mShoppingCartMgr.updateActiveId(info.barCode, mActId);
			}else{
				final ActiveInfo aInfo = info.activeInfo;
				if(aInfo != null && aInfo.getActiveId() != null){
					mShoppingCartMgr.updateActiveId(info.barCode, aInfo.getActiveId());
				}else{
					mShoppingCartMgr.updateActiveId(info.barCode, "");
				}
			}
			Toast.makeText(MyApplication.GetInstance(), "加入购物车成功", Toast.LENGTH_SHORT).show();
			if(!isPrivilege){
				fetchPriceInfo();
			}
		}
	}
	
	private void fetchPriceInfo(){
		mHttpRequester.post(Constants.getIndexUrl()+"?r=cart/padcartlist", makeJsonString(), new INetWorkCallBack() {
			@Override
			public void PostProcess(int msgId, String msg) {
				if(msgId == MyApplication.GetInstance().MSG_TYPE_VALUE){
					JSONObject mgsParse = JSONObject.parseObject(msg);
					JSONObject data = mgsParse.getJSONObject("data");
					JSONObject columns = data.getJSONObject("active_columns");
					for(String key :columns.keySet()){
						JSONObject column_JOBJ = columns.getJSONObject(key);
						final String totalPrice = column_JOBJ.getString("total_price");
						final String cast = column_JOBJ.getString("active_price");
						final String cut = column_JOBJ.getString("preferentialPrice");
						final String comment = column_JOBJ.getString("desc_fee");
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								if(mView != null){
									mView.setPrice(totalPrice, cast, cut,comment);
								}
							}
						});
					}
				}
			}
		});
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
		for(ShoppingCartInfo cartInfo : ShoppingCartMgr.getInstance().getAll()){
			if(cartInfo.activeId.equals(mActId)){
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
		}
		param.put("data", dataArr);
		return param.toString();
	}
}
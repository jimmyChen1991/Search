package com.hhyg.TyClosing.presenter;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.allShop.proc.BaseSearchGoodProc;
import com.hhyg.TyClosing.allShop.proc.FirstSearchGoodPrco;
import com.hhyg.TyClosing.allShop.proc.SearchGoodProc;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.view.SearchGoodView;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
public class SearchGoodPresenter extends BasePresenter<SearchGoodView>{
	private SearchInfo mSearchInfo;
	private String SEARCHGOOD_URI = Constants.getIndexUrl()+"?r=search/search";
	private SearchGoodProc mSessionSearchGoodProc;
	private FirstSearchGoodPrco mFirstSearchGoodPrco;
	private SearchResultInfo SearchResult;
	private SearchGoodMgr mSearchGoodMgr;
	private UiHandler mUiHandler;
	private SrollUiHandler mSrollUiHandler;
	public SearchGoodPresenter(SearchGoodMgr mgr) {
		super();
		mSearchGoodMgr = mgr;
		init();
		
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
	
	private void fetchLaetedSearchResult(Handler handler,BaseSearchGoodProc proc){
		mHttpRequester.post(SEARCHGOOD_URI,MakeJsonString(), new NetCallBackHandlerException(handler,proc));
	}
	protected String MakeJsonString() {
		JSONObject param = new JSONObject();
			param.put("imei", MyApplication.GetInstance().getAndroidId());
			param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
			param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
			JSONObject data = new JSONObject();
			data.put("usersignmd5", ClosingRefInfoMgr.getInstance().getSalerId());
			data.put("keyword",mSearchInfo.key);
			data.put("cat_id", mSearchInfo.cateId);
			data.put("brand_id", mSearchInfo.brandId);
			data.put("sort_field", mSearchInfo.sortfieId);
			data.put("price_min", mSearchInfo.priceInfo.MinPrice);
			data.put("price_max", mSearchInfo.priceInfo.MaxPrice);
			data.put("returncateandbrand", mSearchInfo.isNeedData);
			if(mSearchInfo.sort != -1){
				data.put("sort", mSearchInfo.sort);
			}
			data.put("pagesize", Constants.PAGE_SIZE);
			data.put("currentpage", mSearchInfo.currerPage);
			data.put("search_type", mSearchInfo.searchType);
			param.put("data", data);
		return param.toString();
	}
	class UiHandler extends Handler{
		@Override
		public void dispatchMessage(Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(MyApplication.GetInstance(), "数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(MyApplication.GetInstance(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
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
			}
			if(mView != null){
				mView.disProgress();
				mView.ResetChoseBtn();
				mView.setScollExceptionCanScoll();
			}
		}
	}
	class SearchSessionOnSearchListener implements BaseSearchGoodProc.OnSearchListener{
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
	class SearchFirstOnSearchListener implements BaseSearchGoodProc.OnSearchListener{
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
		mUiHandler = new UiHandler();
		mSrollUiHandler = new SrollUiHandler();
		mSessionSearchGoodProc = new SearchGoodProc();
		mSessionSearchGoodProc.setOnSearchListener(new SearchSessionOnSearchListener());
		mFirstSearchGoodPrco = new FirstSearchGoodPrco();
		mFirstSearchGoodPrco.setOnSearchListener(new SearchFirstOnSearchListener());
	}
}
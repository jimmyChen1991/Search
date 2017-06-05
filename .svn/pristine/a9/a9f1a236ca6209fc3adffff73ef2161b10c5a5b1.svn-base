package com.hhyg.TyClosing.allShop.proc;

import java.io.IOException;
import java.util.ArrayList;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
public abstract class BaseSearchGoodProc implements ProcMsgHelper{
	protected ArrayList<GoodItemInfo> result;
	protected SearchResultInfo mSearchResultInfo;
	protected JSONObject mData;
	protected OnSearchListener mOnSearchListener;
	protected BaseSearchGoodProc() {
		super();
		mSearchResultInfo = new SearchResultInfo();
	}
	public ArrayList<GoodItemInfo> getSearchGood(){
		return result;
	}
	public SearchResultInfo getSearchResult(){
		return mSearchResultInfo;
	}
	public void setOnSearchListener(OnSearchListener listener){
		this.mOnSearchListener = listener;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void ProcMsg(String msgBody) throws IOException,JSONException {
		JSONObject jsonObj = JSONObject.parseObject(msgBody);
		JSONObject data = jsonObj.getJSONObject("data");
		mData = data;
		mSearchResultInfo.totalPage = data.getIntValue("totalpage");
		JSONArray dataArray = data.getJSONArray("goods");
		result = new ArrayList<GoodItemInfo>();
		for(int idx = 0;idx<dataArray.size();idx++){
			GoodItemInfo goodInfo = new GoodItemInfo();
			JSONObject jsonobj = dataArray.getJSONObject(idx);
			goodInfo.barCode = jsonobj.getString("barcode");
			goodInfo.activeCut = jsonobj.getString("active_cut");
			goodInfo.activePrice= jsonobj.getString("active_price");
			goodInfo.brandName = jsonobj.getString("brandname");
			goodInfo.name = jsonobj.getString("name");
			goodInfo.citPrice = jsonobj.getString("price");
			goodInfo.netUri = jsonobj.getString("image");
			goodInfo.full = jsonobj.getString("full");
			goodInfo.stock = jsonobj.getIntValue("stock");
			JSONObject aInfo_JOBJ = jsonobj.getJSONObject("activeinfo");
			if(aInfo_JOBJ != null){
				ActiveInfo aInfo = ActiveInfoProc.procActiveInfo(aInfo_JOBJ);
				goodInfo.activeInfo = aInfo;
			}
			result.add(goodInfo);
		}
		mSearchResultInfo.GoodsInfo = (ArrayList<GoodItemInfo>) result.clone();
	}
	public interface OnSearchListener{
		void OnSearchCompleted();
	}
}

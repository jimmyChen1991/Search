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
public abstract class BaseActiveGoodProc implements ProcMsgHelper{
	protected ArrayList<GoodItemInfo> result;
	protected SearchResultInfo mSearchResultInfo;
	protected JSONObject mData;
	protected OnSearchListener mOnSearchListener;
	protected BaseActiveGoodProc() {
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
		mSearchResultInfo.totalPage = data.getIntValue("totalPages");
		mSearchResultInfo.title = data.getString("title");
		mSearchResultInfo.detail = data.getString("detail");
		JSONArray dataArray = data.getJSONArray("goodsList");
		result = new ArrayList<GoodItemInfo>();
		if(dataArray == null){
			throw new JSONException();
		}
		for(int idx = 0;idx<dataArray.size();idx++){
			GoodItemInfo goodInfo = new GoodItemInfo();
			JSONObject jsonobj = dataArray.getJSONObject(idx);
			goodInfo.barCode = jsonobj.getString("barcode");
			goodInfo.brandName = jsonobj.getString("brand_name");
			goodInfo.name = jsonobj.getString("name");
			goodInfo.citPrice = jsonobj.getString("citprice");
			goodInfo.netUri = jsonobj.getString("image");
			goodInfo.stock = jsonobj.getIntValue("stock");
			goodInfo.attr = jsonobj.getString("attr_info");
			goodInfo.shelveStatus = 1;
			ActiveInfo aInfo = new ActiveInfo();
			if(jsonobj.getString("price") != null){
				aInfo.setActive_price(jsonobj.getString("price"));
			}
			aInfo.setType(jsonobj.getString("active_type"));
			if(jsonobj.getString("active_detail") != null){
				aInfo.setShort_desc(jsonobj.getString("active_detail"));
			}
			aInfo.setActiveId(jsonobj.getString("active_code"));
			goodInfo.activeInfo = aInfo;
			result.add(goodInfo);
		}
		mSearchResultInfo.GoodsInfo = (ArrayList<GoodItemInfo>) result.clone();
	}
	public interface OnSearchListener{
		void OnSearchCompleted();
	}
}

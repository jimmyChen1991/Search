package com.hhyg.TyClosing.allShop.proc;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.CateInfo;
import com.hhyg.TyClosing.allShop.info.SimpleBrandInfo;

public class FirstSearchGoodPrco extends BaseSearchGoodProc{
	@Override
	public void ProcMsg(String msgBody) throws JSONException, IOException {
		super.ProcMsg(msgBody);
		mSearchResultInfo.BrandsInfo = procBrand(mData);
		mSearchResultInfo.rootCateInfo = procCate(mData);
		mOnSearchListener.OnSearchCompleted();
	}
	private ArrayList<SimpleBrandInfo> procBrand(JSONObject data)  throws JSONException{
		JSONArray dataArray = data.getJSONArray("brandids");
		ArrayList<SimpleBrandInfo> result = new ArrayList<SimpleBrandInfo>();
		for(int idx = 0;idx < dataArray.size();idx++){
			SimpleBrandInfo info  = new SimpleBrandInfo();
			JSONObject json = dataArray.getJSONObject(idx);
			info.BrandId = json.getString("cit_brand_id");
			info.BrandName = json.getString("brandname");
			result.add(info);
		}
		return result;
	}
	private CateInfo procCate(JSONObject data)  throws JSONException{
		JSONArray dataArray = data.getJSONArray("cates");
		CateInfo result = new CateInfo();
		ArrayList<CateInfo> secondCateInfos = new ArrayList<CateInfo>();
		for(int idx = 0;idx < dataArray.size();idx++){
			CateInfo info  = new CateInfo();
			JSONObject json = dataArray.getJSONObject(idx);
			info.cateId = json.getString("id");
			info.name = json.getString("name");
			JSONArray thirdArrray = json.getJSONArray("subcate");
			ArrayList<CateInfo> thridCateInfos = new ArrayList<CateInfo>(); 
			for(int idx1 = 0;idx1<thirdArrray.size();idx1++){
				JSONObject thirdJson = thirdArrray.getJSONObject(idx1);
				CateInfo thirdinfo  = new CateInfo();
				thirdinfo.cateId = thirdJson.getString("id");
				thirdinfo.name = thirdJson.getString("name");
				thridCateInfos.add(thirdinfo);
			}
			info.childInfos = thridCateInfos;
			secondCateInfos.add(info);
		}
		result.childInfos = secondCateInfos;
		return result;
	}
	
}

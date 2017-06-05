package com.hhyg.TyClosing.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.*;
import com.hhyg.TyClosing.view.BrandTitleView;
public class BrandTitlePresenter extends BasePresenter<BrandTitleView>{
	private HttpUtil mHttpUtil = (HttpUtil) MyApplication.GetInstance();
	private final String BRAND_URI = Constants.getIndexUrl()+"?r=brand/brand";
	private ProcMsgHelper mMsgProcer;
	private Map<String,ArrayList<BrandImgInfo>> mImgMap;
	public BrandTitlePresenter() {	
		super();
		init();
	}
	public void fetchLastestBrand(){		
		fetchBrandAsync();
	}
	public ArrayList<BrandImgInfo> getLetterInfo(String letter){		
		 ArrayList<BrandImgInfo> ar = mImgMap.get(letter);
		 return ar;		
	}
	private void init(){
		mImgMap = new HashMap<String,ArrayList<BrandImgInfo>>();
		mMsgProcer = new BrandProc();
		//mHotBrand = new ArrayList<BrandImgInfo>();
	}
	private void fetchBrandAsync(){		
		mHttpUtil.post(BRAND_URI, JsonPostParamBuilder.makeParam(), new NetCallBackHandlerException(mSimpleProgressHandler,mMsgProcer));
	}
	
	class BrandProc implements ProcMsgHelper{
		@SuppressWarnings("unchecked")
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			JSONObject data = jsonObj.getJSONObject("data");
			JSONArray hotbrands = data.getJSONArray("hotbrand");
			ArrayList<BrandImgInfo> hotBrands = new ArrayList<BrandImgInfo>();
			for(int idx = 0;idx<hotbrands.size();idx++){
				JSONObject json = hotbrands.getJSONObject(idx);
				BrandImgInfo info = new BrandImgInfo();
				info.id = json.getString("brandid");
				info.netUri = json.getString("url");
				hotBrands.add(info);
			}
			mImgMap.put("热门品牌", hotBrands);
			JSONArray ar = data.getJSONArray("keys");
			ArrayList<String> result = new ArrayList<String>();
			result.add("热门品牌");
			ArrayList<BrandImgInfo> imgAr = new ArrayList<BrandImgInfo>();
			JSONObject info = data.getJSONObject("info");
			for(int idx = 0;idx<ar.size();idx++){
				result.add(ar.getString(idx));			
			}
			for(int index = 1; index<result.size();index++){
				String letter = result.get(index);
				JSONArray jsonAr = info.getJSONArray(letter);				
				for(int idx = 0;idx<jsonAr.size();idx++){
					BrandImgInfo imgInfo = new BrandImgInfo();
					JSONObject json = jsonAr.getJSONObject(idx);
					String brandid = json.getString("brandid");					
					String uri = json.getString("url");
					String name = json.getString("name");
					imgInfo.id = brandid;
					imgInfo.netUri = uri;
					imgInfo.name = name;
					imgAr.add(imgInfo);					
				}
				mImgMap.put(letter, (ArrayList<BrandImgInfo>) imgAr.clone());
				imgAr.clear();
			}
			final ArrayList<String> letterResult = (ArrayList<String>) result.clone();
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					if(mView != null){
						mView.onFetchedBrand(letterResult);	
					}
				}});					
		}
	}	
}
